package com.levelupgamer.autenticacion;

import com.levelupgamer.autenticacion.dto.ChangePasswordRequest;
import com.levelupgamer.autenticacion.dto.LoginResponseDTO;
import com.levelupgamer.autenticacion.dto.RefreshTokenRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AutenticacionController {
    private final AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> iniciarSesion(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponseDTO respuesta = autenticacionService.login(loginRequest);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> renovarToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequest) {
        LoginResponseDTO respuesta = autenticacionService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> actualizarContrasena(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        autenticacionService.changePassword(changePasswordRequest);
        return ResponseEntity.ok().build();
    }
}
