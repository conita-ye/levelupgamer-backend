package com.levelupgamer.usuarios;

import com.levelupgamer.gamificacion.PuntosService;
import com.levelupgamer.gamificacion.dto.PuntosDTO;
import com.levelupgamer.usuarios.dto.UsuarioRegistroDTO;
import com.levelupgamer.usuarios.dto.UsuarioRespuestaDTO;
import com.levelupgamer.usuarios.dto.UsuarioUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PuntosService puntosService;

    public UsuarioController(UsuarioService usuarioService, PuntosService puntosService) {
        this.usuarioService = usuarioService;
        this.puntosService = puntosService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioRespuestaDTO> crearCuenta(@Valid @RequestBody UsuarioRegistroDTO dto) {
        UsuarioRespuestaDTO nuevoUsuario = usuarioService.registrarUsuario(dto);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRespuestaDTO> obtenerPerfil(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    PuntosDTO puntosDTO = puntosService.obtenerPuntosPorUsuario(id);
                    return UsuarioMapper.toDTO(usuario, puntosDTO.getPuntosAcumulados());
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespuestaDTO> modificarPerfil(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto) {
        UsuarioRespuestaDTO usuarioActualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> obtenerRolesDisponibles() {
        return ResponseEntity.ok(RolUsuario.values());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<java.util.List<UsuarioRespuestaDTO>> obtenerTodosLosUsuarios() {
        java.util.List<UsuarioRespuestaDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/admin")
    public ResponseEntity<UsuarioRespuestaDTO> crearAdministrador(@Valid @RequestBody UsuarioRegistroDTO dto) {
        UsuarioRespuestaDTO admin = usuarioService.crearUsuarioAdmin(dto);
        return ResponseEntity.ok(admin);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        boolean eliminado = usuarioService.eliminarUsuario(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
