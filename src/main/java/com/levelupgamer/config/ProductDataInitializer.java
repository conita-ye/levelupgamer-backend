package com.levelupgamer.config;

import com.levelupgamer.productos.Producto;
import com.levelupgamer.productos.ProductoRepository;
import com.levelupgamer.productos.categorias.Categoria;
import com.levelupgamer.productos.categorias.CategoriaRepository;
import com.levelupgamer.usuarios.RolUsuario;
import com.levelupgamer.usuarios.Usuario;
import com.levelupgamer.usuarios.UsuarioRepository;
import java.math.BigDecimal;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@Profile("!test")
@Order(2)
public class ProductDataInitializer implements CommandLineRunner {

        private final ProductoRepository productoRepository;
        private final CategoriaRepository categoriaRepository;
        private final UsuarioRepository usuarioRepository;

        @Value("${aws.s3.bucket.url:}")
        private String s3BucketUrl;
        
        @Value("${storage.provider:local}")
        private String storageProvider;

        public ProductDataInitializer(ProductoRepository productoRepository, CategoriaRepository categoriaRepository,
                        UsuarioRepository usuarioRepository) {
                this.productoRepository = productoRepository;
                this.categoriaRepository = categoriaRepository;
                this.usuarioRepository = usuarioRepository;
        }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (productoRepository.count() == 0) {
            createProducts();
            System.out.println("✓ Catálogo inicial de productos creado correctamente");
        } else {
            System.out.println("ℹ Base de datos ya contiene productos. Se omitió la creación del catálogo inicial.");
        }
    }

        @SuppressWarnings("null")
        private void createProducts() {
        Usuario levelUpVendor = resolveLevelUpVendor();
        Categoria juegosMesa = obtenerOCrearCategoria("JUEGOS_MESA", "Juegos de Mesa",
                "Juegos de estrategia y mesa para toda la familia");
        Categoria accesorios = obtenerOCrearCategoria("ACCESORIOS", "Accesorios",
                "Periféricos y accesorios gamer");
        Categoria consolas = obtenerOCrearCategoria("CONSOLAS", "Consolas",
                "Consolas de última generación");
        Categoria computadores = obtenerOCrearCategoria("COMPUTADORES_GAMERS", "Computadores Gamers",
                "Equipos de alto rendimiento");
        Categoria sillas = obtenerOCrearCategoria("SILLAS_GAMERS", "Sillas Gamers",
                "Sillas ergonómicas para largas sesiones");
        Categoria mouse = obtenerOCrearCategoria("MOUSE", "Mouse Gamer",
                "Mouse diseñados para gaming");
        Categoria mousepad = obtenerOCrearCategoria("MOUSEPAD", "Mousepad",
                "Superficies para precisión");
        Categoria poleras = obtenerOCrearCategoria("POLERAS_PERSONALIZADAS", "Poleras Personalizadas",
                "Merchandising gamer personalizable");
        Categoria perifericos = obtenerOCrearCategoria("PERIFERICOS", "Periféricos",
                "Teclados, mouse y otros accesorios");
        Categoria audio = obtenerOCrearCategoria("AUDIO", "Audio",
                "Auriculares y equipos de audio gamer");
        Categoria monitores = obtenerOCrearCategoria("MONITORES", "Monitores",
                "Monitores gaming de alta calidad");

        // JM001 - Catan
        Producto jm001 = Producto.builder()
                .codigo("JM001")
                .nombre("Catan")
                .descripcion("Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches de juego en familia o con amigos.")
                .precio(new BigDecimal("29990"))
                .stock(15)
                .stockCritico(5)
                .categoria(juegosMesa)
                .puntosLevelUp(200)
                .imagenes(Collections.singletonList(buildProductImage("JM001", "https://kronosgaming.cl/wp-content/uploads/2021/10/1-3.png")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(jm001);

        // JM002 - Carcassonne
        Producto jm002 = Producto.builder()
                .codigo("JM002")
                .nombre("Carcassonne")
                .descripcion("Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne. Ideal para 2-5 jugadores y fácil de aprender.")
                .precio(new BigDecimal("24990"))
                .stock(12)
                .stockCritico(4)
                .categoria(juegosMesa)
                .puntosLevelUp(200)
                .imagenes(Collections.singletonList(buildProductImage("JM002", "https://kronosgaming.cl/wp-content/uploads/2021/10/1-3.png")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(jm002);

        // AC001 - Controlador Inalámbrico Xbox Series X
        Producto ac001 = Producto.builder()
                .codigo("AC001")
                .nombre("Controlador Inalámbrico Xbox Series X")
                .descripcion("Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada. Compatible con consolas Xbox y PC.")
                .precio(new BigDecimal("59990"))
                .stock(8)
                .stockCritico(3)
                .categoria(accesorios)
                .puntosLevelUp(300)
                .imagenes(Collections.singletonList(buildProductImage("AC001", "https://cdnx.jumpseller.com/mundotek/image/42970522/resize/753/753?1701399702")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(ac001);

        // AC002 - Auriculares Gamer HyperX Cloud II
        Producto ac002 = Producto.builder()
            .codigo("AC002")
            .nombre("Auriculares Gamer HyperX Cloud II")
            .descripcion("Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica para mayor comodidad durante largas sesiones de juego.")
            .precio(new BigDecimal("79990"))
            .stock(6)
            .stockCritico(2)
            .categoria(accesorios)
            .puntosLevelUp(400)
            .imagenes(Collections.singletonList(buildProductImage("AC002", "https://cdnx.jumpseller.com/mundotek/image/42970522/resize/753/753?1701399702")))
            .activo(true)
            .vendedor(levelUpVendor)
            .build();
        productoRepository.save(ac002);

        // CO001 - PlayStation 5
        Producto co001 = Producto.builder()
                .codigo("CO001")
                .nombre("PlayStation 5")
                .descripcion("La consola de última generación de Sony, que ofrece gráficos impresionantes y tiempos de carga ultrarrápidos para una experiencia de juego inmersiva.")
                .precio(new BigDecimal("549990"))
                .stock(3)
                .stockCritico(1)
                .categoria(consolas)
                .puntosLevelUp(800)
                .imagenes(Collections.singletonList(buildProductImage("CO001", "https://clsonyb2c.vtexassets.com/arquivos/ids/465203-1600-auto?v=638660598807800000&width=1600&height=auto&aspect=true")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(co001);

        // CG001 - PC Gamer ASUS ROG Strix
        Producto cg001 = Producto.builder()
                .codigo("CG001")
                .nombre("PC Gamer ASUS ROG Strix")
                .descripcion("Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes para ofrecer un rendimiento excepcional en cualquier juego.")
                .precio(new BigDecimal("1299990"))
                .stock(2)
                .stockCritico(1)
                .categoria(computadores)
                .puntosLevelUp(1000)
                .imagenes(Collections.singletonList(buildProductImage("CG001", "https://m.media-amazon.com/images/I/71ENeVg0MuL._AC_SX466_.jpg")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(cg001);

        // SG001 - Silla Gamer SecretLab Titan
        Producto sg001 = Producto.builder()
                .codigo("SG001")
                .nombre("Silla Gamer SecretLab Titan")
                .descripcion("Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable para sesiones de juego prolongadas.")
                .precio(new BigDecimal("349990"))
                .stock(4)
                .stockCritico(1)
                .categoria(sillas)
                .puntosLevelUp(300)
                .imagenes(Collections.singletonList(buildProductImage("SG001", "https://media.falabella.com/falabellaCL/129757370_01/w=1200,h=1200,fit=pad")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(sg001);

        // MS001 - Mouse Gamer Logitech G502 HERO
        Producto ms001 = Producto.builder()
                .codigo("MS001")
                .nombre("Mouse Gamer Logitech G502 HERO")
                .descripcion("Con sensor de alta precisión y botones personalizables, este mouse es ideal para gamers que buscan un control preciso y personalización.")
                .precio(new BigDecimal("49990"))
                .stock(10)
                .stockCritico(3)
                .categoria(mouse)
                .puntosLevelUp(200)
                .imagenes(Collections.singletonList(buildProductImage("MS001", "https://rimage.ripley.cl/home.ripley/Attachment/MKP/1299/MPM00021978732/Image-1.jpg")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(ms001);

        // MP001 - Mousepad Razer Goliathus Extended Chroma
        Producto mp001 = Producto.builder()
                .codigo("MP001")
                .nombre("Mousepad Razer Goliathus Extended Chroma")
                .descripcion("Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme para el movimiento del mouse.")
                .precio(new BigDecimal("29990"))
                .stock(15)
                .stockCritico(5)
                .categoria(mousepad)
                .puntosLevelUp(100)
                .imagenes(Collections.singletonList(buildProductImage("MP001", "https://picsum.photos/seed/levelup-mousepad/800/800")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(mp001);

        // PP001 - Polera Gamer Personalizada 'Level-Up'
        Producto pp001 = Producto.builder()
                .codigo("PP001")
                .nombre("Polera Gamer Personalizada 'Level-Up'")
                .descripcion("Una camiseta cómoda y estilizada, con la posibilidad de personalizarla con tu gamer tag o diseño favorito.")
                .precio(new BigDecimal("14990"))
                .stock(20)
                .stockCritico(8)
                .categoria(poleras)
                .puntosLevelUp(100)
                .imagenes(Collections.singletonList(buildProductImage("PP001", "https://picsum.photos/seed/levelup-tshirt/800/800")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(pp001);

        // KB-RGB-001 - Teclado Mecánico RGB Gamer
        Producto kb001 = Producto.builder()
                .codigo("KB-RGB-001")
                .nombre("Teclado Mecánico RGB Gamer")
                .descripcion("Teclado mecánico con switches azules, iluminación RGB personalizable y reposamanos extraíble. Perfecto para gaming y productividad.")
                .precio(new BigDecimal("89990"))
                .stock(15)
                .stockCritico(5)
                .categoria(perifericos)
                .puntosLevelUp(400)
                .imagenes(Collections.singletonList(buildProductImage("KB-RGB-001", "https://kronosgaming.cl/wp-content/uploads/2021/10/1-3.png")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(kb001);

        // HS-7.1-002 - Audífonos Gamer 7.1 Surround
        Producto hs002 = Producto.builder()
                .codigo("HS-7.1-002")
                .nombre("Audífonos Gamer 7.1 Surround")
                .descripcion("Audífonos con sonido envolvente 7.1, micrófono retráctil con cancelación de ruido e iluminación RGB.")
                .precio(new BigDecimal("69990"))
                .stock(23)
                .stockCritico(8)
                .categoria(audio)
                .puntosLevelUp(300)
                .imagenes(Collections.singletonList(buildProductImage("HS-7.1-002", "https://cdnx.jumpseller.com/mundotek/image/42970522/resize/753/753?1701399702")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(hs002);

        // CH-PRO-003 - Silla Gamer Pro Ergonómica
        Producto ch003 = Producto.builder()
                .codigo("CH-PRO-003")
                .nombre("Silla Gamer Pro Ergonómica")
                .descripcion("Silla ergonómica con soporte lumbar ajustable, reposabrazos 4D y reclinación hasta 180°. Ideal para largas sesiones.")
                .precio(new BigDecimal("249990"))
                .stock(8)
                .stockCritico(3)
                .categoria(sillas)
                .puntosLevelUp(500)
                .imagenes(Collections.singletonList(buildProductImage("CH-PRO-003", "https://media.falabella.com/falabellaCL/129757370_01/w=1200,h=1200,fit=pad")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(ch003);

        // MON-144-004 - Monitor Gaming 144Hz 27"
        Producto mon004 = Producto.builder()
                .codigo("MON-144-004")
                .nombre("Monitor Gaming 144Hz 27\"")
                .descripcion("Monitor curvo 27 pulgadas, tasa de refresco 144Hz, tiempo de respuesta 1ms, resolución QHD 2K.")
                .precio(new BigDecimal("349990"))
                .stock(12)
                .stockCritico(4)
                .categoria(monitores)
                .puntosLevelUp(600)
                .imagenes(Collections.singletonList(buildProductImage("MON-144-004", "https://www.winpy.cl/files/w19813_lg_ultragear_27gl650f-b.jpg")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(mon004);

        // PS5-CTRL-005 - Control DualSense PS5
        Producto ps5005 = Producto.builder()
                .codigo("PS5-CTRL-005")
                .nombre("Control DualSense PS5")
                .descripcion("Control inalámbrico PlayStation 5 con retroalimentación háptica y gatillos adaptativos.")
                .precio(new BigDecimal("59990"))
                .stock(30)
                .stockCritico(10)
                .categoria(accesorios)
                .puntosLevelUp(200)
                .imagenes(Collections.singletonList(buildProductImage("PS5-CTRL-005", "https://clsonyb2c.vtexassets.com/arquivos/ids/465203-1600-auto?v=638660598807800000&width=1600&height=auto&aspect=true")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(ps5005);

        // PC-RTX-006 - PC Gamer RTX 4060
        Producto pc006 = Producto.builder()
                .codigo("PC-RTX-006")
                .nombre("PC Gamer RTX 4060")
                .descripcion("Computador gamer completo: Intel i7 13va gen, RTX 4060 8GB, 16GB RAM, SSD 1TB NVMe, RGB.")
                .precio(new BigDecimal("1299990"))
                .stock(5)
                .stockCritico(2)
                .categoria(computadores)
                .puntosLevelUp(1000)
                .imagenes(Collections.singletonList(buildProductImage("PC-RTX-006", "https://m.media-amazon.com/images/I/71ENeVg0MuL._AC_SX466_.jpg")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(pc006);

        // MOU-RGB-007 - Mouse Gaming RGB 16000 DPI
        Producto mou007 = Producto.builder()
                .codigo("MOU-RGB-007")
                .nombre("Mouse Gaming RGB 16000 DPI")
                .descripcion("Mouse óptico con sensor de alta precisión, 7 botones programables y peso ajustable.")
                .precio(new BigDecimal("39990"))
                .stock(42)
                .stockCritico(15)
                .categoria(mouse)
                .puntosLevelUp(200)
                .imagenes(Collections.singletonList(buildProductImage("MOU-RGB-007", "https://rimage.ripley.cl/home.ripley/Attachment/MKP/1299/MPM00021978732/Image-1.jpg")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(mou007);

        // KIT-STREAM-008 - Kit Streaming Completo
        Producto kit008 = Producto.builder()
                .codigo("KIT-STREAM-008")
                .nombre("Kit Streaming Completo")
                .descripcion("Kit para streamers: micrófono condensador, brazo articulado, luz LED ring, cámara web 1080p.")
                .precio(new BigDecimal("299990"))
                .stock(3)
                .stockCritico(1)
                .categoria(audio)
                .puntosLevelUp(700)
                .imagenes(Collections.singletonList(buildProductImage("KIT-STREAM-008", "https://m.media-amazon.com/images/I/61sa7TrZZzL._AC_SL1280_.jpg")))
                .activo(true)
                .vendedor(levelUpVendor)
                .build();
        productoRepository.save(kit008);
    }

    private String buildProductImage(String codigo, String defaultUrl) {
        // Si está configurado S3, usar URLs de S3
        if ("s3".equalsIgnoreCase(storageProvider) && StringUtils.hasText(s3BucketUrl)) {
            String base = s3BucketUrl.endsWith("/") ? s3BucketUrl.substring(0, s3BucketUrl.length() - 1) : s3BucketUrl;
            return base + "/products/" + codigo + ".jpg";
        }
        // Si no, usar URLs externas (para desarrollo)
        return defaultUrl != null ? defaultUrl : "https://picsum.photos/seed/levelup-" + codigo + "/800/800";
    }

        @SuppressWarnings("null")
        private Usuario resolveLevelUpVendor() {
                return usuarioRepository.findFirstByRolesContaining(RolUsuario.ADMINISTRADOR)
                                .orElseThrow(() -> new IllegalStateException(
                                                "Debe existir al menos un usuario ADMINISTRADOR para asociar los productos seed a LevelUp"));
        }

    @SuppressWarnings("null")
    private Categoria obtenerOCrearCategoria(String codigo, String nombre, String descripcion) {
        Categoria existente = categoriaRepository.findByCodigoIgnoreCase(codigo).orElse(null);
        if (existente != null) {
            return existente;
        }

        Categoria nueva = Categoria.builder()
                .codigo(codigo)
                .nombre(nombre)
                .descripcion(descripcion)
                .activo(true)
                .build();
        categoriaRepository.save(nueva);
        return categoriaRepository.findByCodigoIgnoreCase(codigo)
                .orElseThrow(() -> new IllegalStateException("No se pudo persistir la categoria " + codigo));
    }
}
