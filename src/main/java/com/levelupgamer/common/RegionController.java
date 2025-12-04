package com.levelupgamer.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {

    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerRegiones() {
        return ResponseEntity.ok(RegionesChile.getRegiones());
    }

    @GetMapping("/comunas")
    public ResponseEntity<Map<String, List<String>>> obtenerComunasPorRegion() {
        return ResponseEntity.ok(RegionesChile.getComunasPorRegion());
    }
}

