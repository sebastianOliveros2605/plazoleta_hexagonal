package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.PedidoInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.PlatoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.UsuarioConPedidosPendientes;
import com.plazoleta.plazoleta_service.domain.model.DetallePedido;
import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.out.IPedidoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.ITrazabilidadClientPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RealizarPedidoServiceTest {

    @Mock
    private IPedidoRepositoryPort pedidoRepositoryPort;

    @Mock
    private IRestauranteRepositoryPort restauranteRepositoryPort;

    @Mock
    private IPlatoRepositoryPort platoRepositoryPort;

    @Mock
    private ITrazabilidadClientPort trazabilidadClientPort;

    @Mock
    private IUsuarioClientPort usuarioClientPort;

    @InjectMocks
    private RealizarPedidoService realizarPedidoService;

    @Test
    void realizarPedido_CuandoDatosValidos_DeberiaGuardarConEstadoPendiente() {
        Pedido pedido = crearPedido(1L, 10);
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        Plato plato = new Plato();
        plato.setId(5L);
        plato.setIdRestaurante(1L);

        when(restauranteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(restaurante));
        when(platoRepositoryPort.buscarPorId(5L)).thenReturn(Optional.of(plato));
        when(pedidoRepositoryPort.clienteConPedidosEnProceso(10)).thenReturn(false);
        when(pedidoRepositoryPort.guardar(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedidoGuardado = invocation.getArgument(0);
            pedidoGuardado.setId(100L);
            return pedidoGuardado;
        });

        realizarPedidoService.realizarPedido(pedido);

        assertEquals(EstadoPedidoEnum.PENDIENTE, pedido.getEstado());
        assertNotNull(pedido.getFechaCreacion());
        verify(pedidoRepositoryPort).guardar(pedido);
        verify(trazabilidadClientPort).registrarCambioEstado(100L, 1L, 10, null, null, EstadoPedidoEnum.PENDIENTE, null, null);
    }

    @Test
    void realizarPedido_SiClienteTienePedidoEnProceso_DebeLanzarExcepcion() {
        Pedido pedido = crearPedido(1L, 10);
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        Plato plato = new Plato();
        plato.setId(5L);
        plato.setIdRestaurante(1L);

        when(restauranteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(restaurante));
        when(platoRepositoryPort.buscarPorId(5L)).thenReturn(Optional.of(plato));
        when(pedidoRepositoryPort.clienteConPedidosEnProceso(10)).thenReturn(true);

        assertThrows(UsuarioConPedidosPendientes.class, () -> realizarPedidoService.realizarPedido(pedido));
        verify(pedidoRepositoryPort, never()).guardar(any());
        verify(trazabilidadClientPort, never()).registrarCambioEstado(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void realizarPedido_SiRestauranteNoExiste_DebeLanzarExcepcion() {
        Pedido pedido = crearPedido(1L, 10);

        when(restauranteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RestauranteNoExisteException.class, () -> realizarPedidoService.realizarPedido(pedido));
        verify(pedidoRepositoryPort, never()).guardar(any());
        verify(trazabilidadClientPort, never()).registrarCambioEstado(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void realizarPedido_SiPlatoNoExiste_DebeLanzarExcepcion() {
        Pedido pedido = crearPedido(1L, 10);
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        when(restauranteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(restaurante));
        when(platoRepositoryPort.buscarPorId(5L)).thenReturn(Optional.empty());

        assertThrows(PlatoNoExisteException.class, () -> realizarPedidoService.realizarPedido(pedido));
        verify(pedidoRepositoryPort, never()).guardar(any());
        verify(trazabilidadClientPort, never()).registrarCambioEstado(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void realizarPedido_SiPlatoEsDeOtroRestaurante_DebeLanzarExcepcion() {
        Pedido pedido = crearPedido(1L, 10);
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        Plato plato = new Plato();
        plato.setId(5L);
        plato.setIdRestaurante(99L);

        when(restauranteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(restaurante));
        when(platoRepositoryPort.buscarPorId(5L)).thenReturn(Optional.of(plato));

        assertThrows(PedidoInvalidoException.class, () -> realizarPedidoService.realizarPedido(pedido));
        verify(pedidoRepositoryPort, never()).guardar(any());
        verify(trazabilidadClientPort, never()).registrarCambioEstado(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void realizarPedido_SiNoTieneDetalle_DebeLanzarExcepcion() {
        Pedido pedido = new Pedido();
        pedido.setIdRestaurante(1L);
        pedido.setIdCliente(10);
        pedido.setDetallePedido(List.of());

        assertThrows(PedidoInvalidoException.class, () -> realizarPedidoService.realizarPedido(pedido));
        verify(pedidoRepositoryPort, never()).guardar(any());
        verify(trazabilidadClientPort, never()).registrarCambioEstado(any(), any(), any(), any(), any(), any(), any(), any());
    }

    private Pedido crearPedido(Long idRestaurante, Integer idCliente) {
        DetallePedido detalle = new DetallePedido();
        detalle.setIdPlato(5L);
        detalle.setCantidad(2);

        Pedido pedido = new Pedido();
        pedido.setIdRestaurante(idRestaurante);
        pedido.setIdCliente(idCliente);
        pedido.setDetallePedido(List.of(detalle));
        return pedido;
    }
}
