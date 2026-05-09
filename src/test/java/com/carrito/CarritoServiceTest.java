package com.carrito;

import com.carrito.dto.AgregarProductoDTO;
import com.carrito.dto.CarritoDTO;
import com.carrito.exception.RecursoNoEncontradoException;
import com.carrito.exception.StockInsuficienteException;
import com.carrito.model.Carrito;
import com.carrito.model.Producto;
import com.carrito.repository.CarritoRepository;
import com.carrito.repository.ProductoRepository;
import com.carrito.service.CarritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CarritoService carritoService;

    private Producto producto;
    private Carrito carrito;

    @BeforeEach
    void setUp() {
        producto = new Producto(1L, "Laptop", 899.99, 10);
        carrito = new Carrito();
    }

    @Test
    void crearCarrito_debeRetornarCarritoVacio() {
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoDTO resultado = carritoService.crearCarrito();

        assertThat(resultado).isNotNull();
        assertThat(resultado.getItems()).isEmpty();
        assertThat(resultado.getTotal()).isEqualTo(0.0);
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void agregarProducto_conStockSuficiente_debeAgregarItem() {
        when(carritoRepository.findByIdWithItems(1L)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        AgregarProductoDTO request = new AgregarProductoDTO(1L, 1L, 2);
        CarritoDTO resultado = carritoService.agregarProducto(request);

        assertThat(resultado).isNotNull();
        assertThat(carrito.getItems()).hasSize(1);
        assertThat(producto.getStock()).isEqualTo(8); // 10 - 2
    }

    @Test
    void agregarProducto_sinStockSuficiente_debeLanzarExcepcion() {
        when(carritoRepository.findByIdWithItems(1L)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        AgregarProductoDTO request = new AgregarProductoDTO(1L, 1L, 999);

        assertThatThrownBy(() -> carritoService.agregarProducto(request))
                .isInstanceOf(StockInsuficienteException.class);
    }

    @Test
    void obtenerCarrito_conIdInexistente_debeLanzarExcepcion() {
        when(carritoRepository.findByIdWithItems(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carritoService.obtenerCarrito(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}
