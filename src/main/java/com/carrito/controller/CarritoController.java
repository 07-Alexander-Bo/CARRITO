package com.carrito.controller;

import com.carrito.dto.AgregarProductoDTO;
import com.carrito.dto.CarritoDTO;
import com.carrito.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para la gestión del carrito de compras.
 *
 * Endpoints:
 *   POST   /api/carritos                    → crearCarrito()
 *   POST   /api/carritos/agregar-producto   → agregarProducto()
 *   GET    /api/carritos/{id}               → obtenerCarrito()
 *   GET    /api/carritos/{id}/total         → calcularTotal()
 */
@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    /**
     * Crea un nuevo carrito vacío.
     * @return 201 Created con el CarritoDTO
     */
    @PostMapping
    public ResponseEntity<CarritoDTO> crearCarrito() {
        CarritoDTO carrito = carritoService.crearCarrito();
        return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
    }

    /**
     * Agrega un producto al carrito.
     * @param request DTO con carritoId, productoId y cantidad
     * @return 200 OK con el CarritoDTO actualizado
     */
    @PostMapping("/agregar-producto")
    public ResponseEntity<CarritoDTO> agregarProducto(@Valid @RequestBody AgregarProductoDTO request) {
        CarritoDTO carrito = carritoService.agregarProducto(request);
        return ResponseEntity.ok(carrito);
    }

    /**
     * Obtiene el carrito con todos sus ítems y el total calculado.
     * @param id ID del carrito
     * @return 200 OK con el CarritoDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> obtenerCarrito(@PathVariable Long id) {
        CarritoDTO carrito = carritoService.obtenerCarrito(id);
        return ResponseEntity.ok(carrito);
    }

    /**
     * Calcula y retorna el total monetario del carrito.
     * @param id ID del carrito
     * @return 200 OK con el total
     */
    @GetMapping("/{id}/total")
    public ResponseEntity<Map<String, Object>> obtenerTotal(@PathVariable Long id) {
        double total = carritoService.calcularTotal(id);
        return ResponseEntity.ok(Map.of(
                "carritoId", id,
                "total", total
        ));
    }
}
