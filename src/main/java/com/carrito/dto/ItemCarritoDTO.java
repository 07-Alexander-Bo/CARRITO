package com.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para un ítem dentro del carrito.
 * Expone únicamente los datos necesarios al cliente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoDTO {

    private String nombreProducto;
    private int cantidad;
    private double subtotal;
}
