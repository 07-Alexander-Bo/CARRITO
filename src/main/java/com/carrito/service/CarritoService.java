package com.carrito.service;

import com.carrito.dto.AgregarProductoDTO;
import com.carrito.dto.CarritoDTO;
import com.carrito.dto.ItemCarritoDTO;
import com.carrito.exception.RecursoNoEncontradoException;
import com.carrito.exception.StockInsuficienteException;
import com.carrito.model.Carrito;
import com.carrito.model.ItemCarrito;
import com.carrito.model.Producto;
import com.carrito.repository.CarritoRepository;
import com.carrito.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para gestión del carrito de compras.
 * Implementa las tres operaciones principales del diagrama:
 *   - crearCarrito()
 *   - agregarProducto()
 *   - calcularTotal()
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    /**
     * Crea un nuevo carrito vacío y lo persiste en la base de datos.
     *
     * @return el CarritoDTO del carrito recién creado
     */
    @Transactional
    public CarritoDTO crearCarrito() {
        Carrito carrito = new Carrito();
        Carrito guardado = carritoRepository.save(carrito);
        log.info("Carrito creado con ID: {}", guardado.getId());
        return mapearCarritoDTO(guardado);
    }

    /**
     * Agrega un producto al carrito indicado.
     * Si el producto ya existe en el carrito, incrementa la cantidad.
     * Descuenta el stock del producto en la base de datos.
     *
     * @param request DTO con carritoId, productoId y cantidad
     * @return el CarritoDTO actualizado
     */
    @Transactional
    public CarritoDTO agregarProducto(AgregarProductoDTO request) {
        Carrito carrito = carritoRepository.findByIdWithItems(request.getCarritoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito", request.getCarritoId()));

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", request.getProductoId()));

        // Validar stock suficiente
        if (producto.getStock() < request.getCantidad()) {
            throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), request.getCantidad());
        }

        // Buscar si el producto ya existe en el carrito para acumular cantidad
        ItemCarrito itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(producto.getId()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setCantidad(itemExistente.getCantidad() + request.getCantidad());
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCantidad(request.getCantidad());
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            nuevoItem.setProducto(producto);
            nuevoItem.setCarrito(carrito);
            carrito.getItems().add(nuevoItem);
        }

        // Descontar stock del producto
        producto.setStock(producto.getStock() - request.getCantidad());
        productoRepository.save(producto);

        Carrito actualizado = carritoRepository.save(carrito);
        log.info("Producto '{}' agregado al carrito {}. Cantidad: {}",
                producto.getNombre(), carrito.getId(), request.getCantidad());

        return mapearCarritoDTO(actualizado);
    }

    /**
     * Calcula el total monetario del carrito.
     *
     * @param carritoId ID del carrito
     * @return el total calculado como double
     */
    @Transactional(readOnly = true)
    public double calcularTotal(Long carritoId) {
        Carrito carrito = carritoRepository.findByIdWithItems(carritoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito", carritoId));
        return carrito.calcularTotal();
    }

    /**
     * Obtiene el carrito completo con todos sus ítems.
     *
     * @param carritoId ID del carrito
     * @return CarritoDTO con ítems y total
     */
    @Transactional(readOnly = true)
    public CarritoDTO obtenerCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findByIdWithItems(carritoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito", carritoId));
        return mapearCarritoDTO(carrito);
    }

    // ─────────────────────── Mapeos DTO ───────────────────────

    private CarritoDTO mapearCarritoDTO(Carrito carrito) {
        List<ItemCarritoDTO> itemsDTO = carrito.getItems().stream()
                .map(this::mapearItemCarritoDTO)
                .collect(Collectors.toList());

        return new CarritoDTO(
                carrito.getId(),
                itemsDTO,
                carrito.calcularTotal()
        );
    }

    private ItemCarritoDTO mapearItemCarritoDTO(ItemCarrito item) {
        return new ItemCarritoDTO(
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.calcularSubtotal()
        );
    }
}
