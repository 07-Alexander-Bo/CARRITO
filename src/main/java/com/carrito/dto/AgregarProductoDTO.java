package com.carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para agregar un producto al carrito.
 * Incluye validaciones de Bean Validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarProductoDTO {

    @NotNull(message = "El ID del carrito es obligatorio")
    private Long carritoId;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;
}
