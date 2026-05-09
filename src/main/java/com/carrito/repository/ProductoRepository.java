package com.carrito.repository;

import com.carrito.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Producto.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /** Busca productos cuyo nombre contenga el texto dado (sin distinción de mayúsculas). */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /** Busca productos con stock disponible mayor a cero. */
    List<Producto> findByStockGreaterThan(int stock);
}
