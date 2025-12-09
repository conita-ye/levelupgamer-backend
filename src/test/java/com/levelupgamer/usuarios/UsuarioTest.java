package com.levelupgamer.usuarios;

import com.levelupgamer.exception.UserAlreadyExistsException;
import com.levelupgamer.gamificacion.Puntos;
import com.levelupgamer.gamificacion.PuntosRepository;
import com.levelupgamer.gamificacion.PuntosService;
import com.levelupgamer.gamificacion.dto.PuntosDTO;
import com.levelupgamer.usuarios.dto.UsuarioRegistroDTO;
import com.levelupgamer.usuarios.dto.UsuarioRespuestaDTO;
import com.levelupgamer.usuarios.dto.UsuarioUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PuntosRepository puntosRepository;

    @Mock
    private PuntosService puntosService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioExistente;
    private UsuarioRegistroDTO registroDTO;
    private UsuarioUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        usuarioExistente = Usuario.builder()
                .id(1L)
                .run("123456789")
                .nombre("Juan")
                .apellidos("Pérez")
                .correo("juan@example.com")
                .contrasena("$2a$10$encoded")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .region("Metropolitana")
                .comuna("Santiago")
                .direccion("Calle 123")
                .roles(Set.of(RolUsuario.CLIENTE))
                .activo(true)
                .isDuocUser(false)
                .build();

        registroDTO = UsuarioRegistroDTO.builder()
                .run("987654321")
                .nombre("María")
                .apellidos("González")
                .correo("maria@example.com")
                .contrasena("password123")
                .fechaNacimiento(LocalDate.of(1995, 5, 15))
                .region("Valparaíso")
                .comuna("Valparaíso")
                .direccion("Av. Principal 456")
                .build();

        updateDTO = UsuarioUpdateDTO.builder()
                .nombre("Juan Actualizado")
                .apellidos("Pérez Actualizado")
                .direccion("Nueva Dirección 789")
                .build();
    }

    @Test
    void registrarUsuario_ConDatosValidos_DeberiaCrearUsuario() {
        when(usuarioRepository.findByCorreo(registroDTO.getCorreo())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registroDTO.getContrasena())).thenReturn("$2a$10$encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(2L);
            return usuario;
        });
        when(puntosRepository.save(any(Puntos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioRespuestaDTO resultado = usuarioService.registrarUsuario(registroDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCorreo()).isEqualTo(registroDTO.getCorreo());
        assertThat(resultado.getNombre()).isEqualTo(registroDTO.getNombre());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(puntosRepository).save(any(Puntos.class));
    }

    @Test
    void registrarUsuario_ConCorreoExistente_DeberiaLanzarExcepcion() {
        when(usuarioRepository.findByCorreo(registroDTO.getCorreo())).thenReturn(Optional.of(usuarioExistente));

        assertThrows(UserAlreadyExistsException.class, () -> {
            usuarioService.registrarUsuario(registroDTO);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_ConDatosValidos_DeberiaActualizarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
        when(puntosService.obtenerPuntosPorUsuario(1L)).thenReturn(new PuntosDTO(1L, 100));

        UsuarioRespuestaDTO resultado = usuarioService.actualizarUsuario(1L, updateDTO);

        assertThat(resultado).isNotNull();
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario usuarioActualizado = captor.getValue();
        assertThat(usuarioActualizado.getNombre()).isEqualTo(updateDTO.getNombre());
        assertThat(usuarioActualizado.getApellidos()).isEqualTo(updateDTO.getApellidos());
    }

    @Test
    void buscarPorId_ConIdExistente_DeberiaRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));

        Optional<Usuario> resultado = usuarioService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        assertThat(resultado.get().getCorreo()).isEqualTo(usuarioExistente.getCorreo());
    }

    @Test
    void buscarPorId_ConIdInexistente_DeberiaRetornarVacio() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarPorId(999L);

        assertThat(resultado).isEmpty();
    }
}

