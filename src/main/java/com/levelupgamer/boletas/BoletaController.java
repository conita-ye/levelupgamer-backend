package com.levelupgamer.boletas;

import com.levelupgamer.boletas.dto.BoletaActualizarEstadoRequest;
import com.levelupgamer.boletas.dto.BoletaCrearRequest;
import com.levelupgamer.boletas.dto.BoletaRespuestaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/boletas")
public class BoletaController {
    @Autowired
    private BoletaService boletaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE')")
    @PostMapping
    public ResponseEntity<BoletaRespuestaDTO> procesarCompra(@Valid @RequestBody BoletaCrearRequest request) {
        BoletaRespuestaDTO nuevaBoleta = boletaService.crearBoleta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaBoleta);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<BoletaRespuestaDTO>> obtenerTodasLasBoletas() {
        List<BoletaRespuestaDTO> boletas = boletaService.listarTodas();
        return ResponseEntity.ok(boletas);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BoletaRespuestaDTO>> obtenerBoletasDelUsuario(@PathVariable Long userId) {
        List<BoletaRespuestaDTO> boletas = boletaService.listarBoletasPorUsuario(userId);
        return ResponseEntity.ok(boletas);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<BoletaRespuestaDTO> obtenerDetalleBoleta(@PathVariable Long id) {
        return boletaService.buscarPorId(id)
                .map(BoletaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<BoletaRespuestaDTO> cambiarEstadoBoleta(
            @PathVariable Long id,
            @Valid @RequestBody BoletaActualizarEstadoRequest request) {
        BoletaRespuestaDTO boletaActualizada = boletaService.actualizarEstado(id, request.getEstado());
        return ResponseEntity.ok(boletaActualizada);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarBoleta(@PathVariable Long id) {
        boletaService.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }
}
