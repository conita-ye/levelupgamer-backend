package com.levelupgamer.autenticacion;

import com.levelupgamer.autenticacion.dto.LoginResponseDTO;
import com.levelupgamer.usuarios.RolUsuario;
import com.levelupgamer.usuarios.Usuario;
import com.levelupgamer.usuarios.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AutenticacionTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AutenticacionService autenticacionService;

    private Usuario usuario;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario = Usuario.builder()
                .id(1L)
                .run("123456789")
                .nombre("Test")
                .apellidos("User")
                .correo("test@example.com")
                .contrasena("$2a$10$encodedPassword")
                .roles(Set.of(RolUsuario.CLIENTE))
                .activo(true)
                .build();

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void login_ConCredencialesValidas_DeberiaRetornarToken() {
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", usuario.getContrasena())).thenReturn(true);

        LoginResponseDTO response = autenticacionService.login(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getUser());
        assertEquals("test@example.com", response.getUser().getEmail());
    }

    @Test
    void login_ConCredencialesInvalidas_DeberiaLanzarExcepcion() {
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrongPassword", usuario.getContrasena())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            autenticacionService.login(loginRequest);
        });
    }

    @Test
    void login_ConUsuarioInexistente_DeberiaLanzarExcepcion() {
        when(usuarioRepository.findByCorreo("nonexistent@example.com")).thenReturn(Optional.empty());

        loginRequest.setEmail("nonexistent@example.com");
        
        assertThrows(BadCredentialsException.class, () -> {
            autenticacionService.login(loginRequest);
        });
    }
}

