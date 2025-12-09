package com.levelupgamer.gamificacion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanjeRequestDTO {
    private Long usuarioId;
    private Long itemId;
    private String tipoItem; // "descuento", "producto", "envio"
    private Integer puntosRequeridos;
    private Integer valor; // Porcentaje de descuento o valor
}
