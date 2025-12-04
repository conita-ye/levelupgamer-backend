package com.levelupgamer.gamificacion;

import com.levelupgamer.gamificacion.dto.PuntosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/points")
public class PuntosController {
    @Autowired
    private PuntosService puntosService;

    @GetMapping("/{userId}")
    public ResponseEntity<PuntosDTO> obtenerSaldoPuntos(@PathVariable Long userId) {
        PuntosDTO puntos = puntosService.obtenerPuntosPorUsuario(userId);
        return ResponseEntity.ok(puntos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CLIENTE')")
    @PostMapping("/earn")
    public ResponseEntity<PuntosDTO> acumularPuntos(@RequestBody PuntosDTO dto) {
        PuntosDTO puntosActualizados = puntosService.sumarPuntos(dto);
        return ResponseEntity.ok(puntosActualizados);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CLIENTE')")
    @PostMapping("/redeem")
    public ResponseEntity<PuntosDTO> usarPuntos(@RequestBody PuntosDTO dto) {
        PuntosDTO puntosActualizados = puntosService.canjearPuntos(dto);
        return ResponseEntity.ok(puntosActualizados);
    }
}
