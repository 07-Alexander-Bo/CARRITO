package com.carrito.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Carrito — representa un carrito de compras con sus ítems.
 *
 * CORRECCIÓN: Se usa @Getter/@Setter en vez de @Data para evitar recursión
 * infinita en equals/hashCode/toString con la relación bidireccional.
 */
@Entity
@Table(name = "carrito")
@Getter
@Setter
@NoArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemCarrito> items = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public double calcularTotal() {
        return items.stream()
                .mapToDouble(ItemCarrito::calcularSubtotal)
                .sum();
    }
}
