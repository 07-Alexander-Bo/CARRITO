package com.carrito.service;

import com.carrito.exception.RecursoNoEncontradoException;
import com.carrito.model.Producto;
import com.carrito.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión del catálogo de productos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }

    @Transactional
    public Producto crearProducto(Producto producto) {
        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado: {} (ID: {})", guardado.getNombre(), guardado.getId());
        return guardado;
    }

    @Transactional
    public Producto actualizarProducto(Long id, Producto datosActualizados) {
        Producto existente = obtenerProducto(id);
        existente.setNombre(datosActualizados.getNombre());
        existente.setPrecio(datosActualizados.getPrecio());
        existente.setStock(datosActualizados.getStock());
        return productoRepository.save(existente);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        Producto existente = obtenerProducto(id);
        productoRepository.delete(existente);
        log.info("Producto eliminado: ID {}", id);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
