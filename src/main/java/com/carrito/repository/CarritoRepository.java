package com.carrito.repository;

import com.carrito.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Carrito.
 * Spring Data genera la implementación en tiempo de ejecución.
 */
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    /**
     * Busca un carrito por ID cargando sus ítems y productos asociados
     * en una sola consulta (evita el problema N+1).
     */
    @Query("SELECT c FROM Carrito c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.producto WHERE c.id = :id")
    Optional<Carrito> findByIdWithItems(@Param("id") Long id);
}
