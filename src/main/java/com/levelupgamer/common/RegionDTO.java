package com.levelupgamer.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private String nombre;
    private List<String> comunas;
}

