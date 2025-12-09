package com.levelupgamer.gamificacion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelUsuarioDTO {
    private String nombreNivel;
    private Integer puntosActuales;
    private Integer puntosMinimos;
    private Integer puntosMaximos;
    private String color;
    private String descripcion;
    private String siguienteNivel;
    private Integer puntosParaSiguienteNivel;
    private Double progresoPorcentaje;
    private java.util.List<String> beneficios;
}
