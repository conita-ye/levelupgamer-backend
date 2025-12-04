package com.levelupgamer.config;

import com.levelupgamer.gamificacion.Puntos;
import com.levelupgamer.gamificacion.PuntosRepository;
import com.levelupgamer.usuarios.RolUsuario;
import com.levelupgamer.usuarios.Usuario;
import com.levelupgamer.usuarios.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;

@Component
@Profile("!test")
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PuntosRepository puntosRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PuntosRepository puntosRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.puntosRepository = puntosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            createUsers();
            System.out.println("✓ Usuarios iniciales creados correctamente");
        } else {
            System.out.println("ℹ Base de datos ya contiene usuarios. Se omitió la creación de usuarios de prueba.");
        }
    }

    @SuppressWarnings("null")
    private void createUsers() {
        // 1. Usuario Admin
        HashSet<RolUsuario> adminRoles = new HashSet<>();
        adminRoles.add(RolUsuario.ADMINISTRADOR);
        adminRoles.add(RolUsuario.CLIENTE);
        Usuario admin = Usuario.builder()
                .run("111111111")
                .nombre("Admin")
                .apellidos("User")
                .correo("admin@gmail.com")
                .contrasena(passwordEncoder.encode("admin123"))
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .region("Metropolitana")
                .comuna("Santiago")
                .direccion("Av. Principal 100")
                .roles(adminRoles)
                .activo(true)
                .isDuocUser(false)
                .build();
        usuarioRepository.save(admin);
        
        Puntos puntosAdmin = Puntos.builder()
                .usuario(admin)
                .puntosAcumulados(1000)
                .build();
        puntosRepository.save(puntosAdmin);

        // 2. Usuario Cliente
        HashSet<RolUsuario> clienteRoles = new HashSet<>();
        clienteRoles.add(RolUsuario.CLIENTE);
        Usuario cliente = Usuario.builder()
                .run("222222222")
                .nombre("Cliente")
                .apellidos("Leal")
                .correo("cliente@gmail.com")
                .contrasena(passwordEncoder.encode("cliente123"))
                .fechaNacimiento(LocalDate.of(1995, 5, 10))
                .region("Valparaíso")
                .comuna("Valparaíso")
                .direccion("Calle Comercial 456")
                .roles(clienteRoles)
                .activo(true)
                .isDuocUser(false)
                .build();
        usuarioRepository.save(cliente);

        Puntos puntosCliente = Puntos.builder()
                .usuario(cliente)
                .puntosAcumulados(50)
                .build();
        puntosRepository.save(puntosCliente);

        // 3. Usuario con Descuento Especial
        HashSet<RolUsuario> coniRoles = new HashSet<>();
        coniRoles.add(RolUsuario.CLIENTE);
        Usuario coni = Usuario.builder()
                .run("333333333")
                .nombre("Coni")
                .apellidos("Ye")
                .correo("coni123@gmail.com")
                .contrasena(passwordEncoder.encode("Coni123"))
                .fechaNacimiento(LocalDate.parse("2000-10-20"))
                .region("Metropolitana")
                .comuna("Santiago")
                .direccion("Av. Principal 123")
                .roles(coniRoles)
                .activo(true)
                .isDuocUser(false)
                .build();
        usuarioRepository.save(coni);
        
        Puntos puntosConi = Puntos.builder()
                .usuario(coni)
                .puntosAcumulados(150)
                .build();
        puntosRepository.save(puntosConi);

        // 4. Usuario Vendedor
        HashSet<RolUsuario> vendedorRoles = new HashSet<>();
        vendedorRoles.add(RolUsuario.VENDEDOR);
        Usuario vendedor = Usuario.builder()
            .run("444444444")
            .nombre("Vendedor")
            .apellidos("Demo")
            .correo("vendedor@gmail.com")
            .contrasena(passwordEncoder.encode("vendedor123"))
            .fechaNacimiento(LocalDate.of(1992, 3, 15))
            .region("Biobío")
            .comuna("Concepción")
            .direccion("Calle Comercio 123")
            .roles(vendedorRoles)
            .activo(true)
            .isDuocUser(false)
            .build();
        usuarioRepository.save(vendedor);

        Puntos puntosVendedor = Puntos.builder()
            .usuario(vendedor)
            .puntosAcumulados(0)
            .build();
        puntosRepository.save(puntosVendedor);
    }
}
