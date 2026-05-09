package com.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta para el carrito completo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDTO {

    private Long id;
    private List<ItemCarritoDTO> items;
    private double total;
}
