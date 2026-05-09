package com.carrito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no hay stock suficiente para satisfacer un pedido.
 * Produce automáticamente una respuesta HTTP 409 Conflict.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String nombreProducto, int stockDisponible, int cantidadSolicitada) {
        super(String.format(
                "Stock insuficiente para '%s'. Disponible: %d, Solicitado: %d.",
                nombreProducto, stockDisponible, cantidadSolicitada
        ));
    }
}
