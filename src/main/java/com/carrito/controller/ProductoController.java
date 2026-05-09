package com.carrito.controller;

import com.carrito.model.Producto;
import com.carrito.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión del catálogo de productos.
 *
 * CORRECCIÓN: El POST y PUT ahora reciben un Map<String,Object> en vez de la
 * entidad directamente, evitando el error 500 por deserialización de JPA entities.
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listar(@RequestParam(required = false) String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
        }
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProducto(id));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Map<String, Object> body) {
        Producto producto = new Producto();
        producto.setNombre((String) body.get("nombre"));
        producto.setPrecio(((Number) body.get("precio")).doubleValue());
        producto.setStock(((Number) body.get("stock")).intValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,
                                                @RequestBody Map<String, Object> body) {
        Producto datos = new Producto();
        datos.setNombre((String) body.get("nombre"));
        datos.setPrecio(((Number) body.get("precio")).doubleValue());
        datos.setStock(((Number) body.get("stock")).intValue());
        return ResponseEntity.ok(productoService.actualizarProducto(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
