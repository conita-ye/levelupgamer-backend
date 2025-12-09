package com.levelupgamer.gamificacion;

import com.levelupgamer.gamificacion.dto.*;
import com.levelupgamer.gamificacion.cupones.Cupon;
import com.levelupgamer.gamificacion.cupones.dto.CuponDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/{userId}/level")
    public ResponseEntity<NivelUsuarioDTO> obtenerNivelUsuario(@PathVariable Long userId) {
        NivelUsuarioDTO nivel = puntosService.obtenerNivelUsuario(userId);
        return ResponseEntity.ok(nivel);
    }

    @GetMapping("/redeemable-items")
    public ResponseEntity<List<ItemCanjeableDTO>> obtenerItemsCanjeables() {
        List<ItemCanjeableDTO> items = puntosService.obtenerItemsCanjeables();
        return ResponseEntity.ok(items);
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

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CLIENTE')")
    @PostMapping("/redeem-item")
    public ResponseEntity<?> canjearItem(@RequestBody CanjeRequestDTO request) {
        Cupon cupon = puntosService.canjearItem(request);
        if (cupon != null) {
            // Convertir Cupon a CuponDTO usando el mapper
            CuponDTO cuponDTO = com.levelupgamer.gamificacion.mapper.CuponMapper.toDTO(cupon);
            return ResponseEntity.ok(cuponDTO);
        }
        // Si no es un cupón, retornar éxito con mensaje
        return ResponseEntity.ok(Map.of("mensaje", "Puntos canjeados exitosamente", "tipo", request.getTipoItem()));
    }
}
