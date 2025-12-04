package com.levelupgamer.config;

import com.levelupgamer.contenido.Blog;
import com.levelupgamer.contenido.BlogRepository;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Component
@Profile("!test")
public class BlogDataInitializer implements CommandLineRunner {

    private final BlogRepository blogRepository;

    @Value("${aws.s3.bucket.name:}")
    private String bucketName;
    
    @Value("${storage.provider:local}")
    private String storageProvider;

    @Value("${blog.seed.local-markdown-dir:s3-files/contenido}")
    private String localMarkdownDir;

    public BlogDataInitializer(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        boolean updated = ensureS3PathConvention();
        if (updated) {
            System.out.println(">>> Se actualizaron las rutas S3 de blogs existentes para utilizar blogs/{id}. <<<");
        }
        if (blogRepository.count() == 0) {
            createBlogs();
            System.out.println("✓ Contenido de blog inicial creado correctamente");
        } else {
            System.out.println("ℹ Base de datos ya contiene blogs. Se omitió la creación de contenido inicial.");
        }
    }

    @SuppressWarnings("null")
    private void createBlogs() {
        // Blog 1 - Configuraciones de PC para Juegos 2025
        Blog blog1 = Blog.builder()
                .titulo("Configuraciones de PC para Juegos 2025")
                .autor("Level-Up Gamer Team - David Hurtado")
                .fechaPublicacion(LocalDate.parse("2025-10-01"))
                .descripcionCorta("Descubre las mejores configuraciones de PC gamer para este año. Analizamos procesadores, tarjetas gráficas y periféricos para cada presupuesto.")
                .altImagen("Configuración de PC para juegos")
                .build();
        persistWithAssetUrls(blog1, "BP001", "https://wallpapers.com/images/featured-full/configuracion-de-pc-para-juegos-hddrabo28apipl3y.jpg");

        // Blog 2 - Top 5 Juegos más Esperados del 2025
        Blog blog2 = Blog.builder()
                .titulo("Top 5 Juegos más Esperados del 2025")
                .autor("Level-Up Gamer Team - Ana Baloa")
                .fechaPublicacion(LocalDate.parse("2025-09-15"))
                .descripcionCorta("Estos son los títulos que están generando más expectativa entre los jugadores: secuelas épicas, nuevas IP y mundos abiertos impresionantes.")
                .altImagen("Juegos más esperados 2025")
                .build();
        persistWithAssetUrls(blog2, "BP002", "https://cdn.hobbyconsolas.com/sites/navi.axelspringer.es/public/media/image/2024/11/20-juegos-multiplataforma-van-definir-2025-4258884.jpg?tf=1200x");

        // Blog 3 - Cómo Mejorar tu Rendimiento en eSports
        Blog blog3 = Blog.builder()
                .titulo("Cómo Mejorar tu Rendimiento en eSports")
                .autor("Level-Up Gamer Team")
                .fechaPublicacion(LocalDate.parse("2025-10-20"))
                .descripcionCorta("Desde la configuración de tu equipo hasta la rutina mental: claves para destacar en el competitivo mundo de los eSports.")
                .altImagen("eSports competitivo")
                .build();
        persistWithAssetUrls(blog3, "BP003", "https://img.redbull.com/images/q_auto,f_auto/redbullcom/2023/5/8/iqc5g861atcofugp4t2n/redbullcampusclutch");
    }

    private boolean ensureS3PathConvention() {
        if (!"s3".equalsIgnoreCase(storageProvider) || !StringUtils.hasText(bucketName)) {
            return false;
        }
        String prefix = "https://" + bucketName + ".s3.amazonaws.com/blogs/";
        boolean updatedAny = false;
        for (Blog blog : blogRepository.findAll()) {
            boolean updated = false;
            if (needsS3Fix(blog.getContenidoUrl(), prefix, blog.getId())) {
                blog.setContenidoUrl(buildContentUrl(blog.getId(), String.valueOf(blog.getId())));
                updated = true;
            }
            if (needsS3Fix(blog.getImagenUrl(), prefix, blog.getId())) {
                blog.setImagenUrl(buildImageUrl(blog.getId(), String.valueOf(blog.getId()), null));
                updated = true;
            }
            if (updated) {
                blogRepository.save(blog);
                updatedAny = true;
            }
        }
        return updatedAny;
    }

    private boolean needsS3Fix(String url, String prefix, Long blogId) {
        if (!StringUtils.hasText(url) || blogId == null) {
            return false;
        }
        return url.startsWith(prefix) && !url.startsWith(prefix + blogId + "/");
    }

    @SuppressWarnings("null")
    private void persistWithAssetUrls(Blog blog, String slug, String defaultImageUrl) {
        Blog persisted = blogRepository.save(blog);
        persisted.setContenidoUrl(buildContentUrl(persisted.getId(), slug));
        persisted.setImagenUrl(buildImageUrl(persisted.getId(), slug, defaultImageUrl));
        blogRepository.save(persisted);
    }

    private String buildContentUrl(Long blogId, String slug) {
        if ("s3".equalsIgnoreCase(storageProvider) && StringUtils.hasText(bucketName) && blogId != null) {
            return "https://" + bucketName + ".s3.amazonaws.com/blogs/" + blogId + "/blog.md";
        }
        return "http://localhost:8081/uploads/contenido/" + slug + ".md";
    }

    private String buildImageUrl(Long blogId, String slug, String defaultImageUrl) {
        if ("s3".equalsIgnoreCase(storageProvider) && StringUtils.hasText(bucketName) && blogId != null) {
            return "https://" + bucketName + ".s3.amazonaws.com/blogs/" + blogId + "/blog.jpg";
        }
        return defaultImageUrl != null ? defaultImageUrl : "https://picsum.photos/seed/levelupgamer-" + slug + "/1200/600";
    }
}
