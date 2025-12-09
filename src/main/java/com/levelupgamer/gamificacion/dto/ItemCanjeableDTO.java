package com.levelupgamer.gamificacion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCanjeableDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer puntosRequeridos;
    private String tipo; // "descuento", "producto", "envio"
    private Integer valor; // Porcentaje de descuento o valor del producto
    private String imagen;
}
