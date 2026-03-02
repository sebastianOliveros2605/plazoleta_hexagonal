package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.UsuarioNoAsociadoRestauranteException;
import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.domain.ports.out.IPedidoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarPedidoServiceTest {

    @Mock
    private IPedidoRepositoryPort pedidoRepositoryPort;

    @Mock
    private IUsuarioClientPort usuarioClientPort;

    @InjectMocks
    private ConsultarPedidoService consultarPedidoService;

    @Test
    void listarPedidosPorEstado_CuandoDatosValidos_DebeRetornarPaginacion() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setIdRestaurante(20L);
        pedido.setEstado(EstadoPedidoEnum.PENDIENTE);
        PaginacionResultado<Pedido> esperado = new PaginacionResultado<>(List.of(pedido), 0, 10, 1, 1, true);

        when(usuarioClientPort.consultarIdRestauranteDeUsuario(7)).thenReturn(20L);
        when(pedidoRepositoryPort.listarPorRestauranteYEstado(20L, EstadoPedidoEnum.PENDIENTE, 0, 10))
                .thenReturn(esperado);

        PaginacionResultado<Pedido> resultado =
                consultarPedidoService.listarPedidosPorEstado(7, EstadoPedidoEnum.PENDIENTE, 0, 10);

        assertSame(esperado, resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(pedidoRepositoryPort).listarPorRestauranteYEstado(20L, EstadoPedidoEnum.PENDIENTE, 0, 10);
    }

    @Test
    void listarPedidosPorEstado_SiUsuarioNoTieneRestaurante_DebeLanzarExcepcion() {
        when(usuarioClientPort.consultarIdRestauranteDeUsuario(7)).thenReturn(null);

        assertThrows(
                UsuarioNoAsociadoRestauranteException.class,
                () -> consultarPedidoService.listarPedidosPorEstado(7, EstadoPedidoEnum.PENDIENTE, 0, 10)
        );

        verify(pedidoRepositoryPort, never()).listarPorRestauranteYEstado(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    void listarPedidosPorEstado_SiEstadoEsListo_DebeUsarElMismoEstadoEnRepositorio() {
        PaginacionResultado<Pedido> esperado = new PaginacionResultado<>(List.of(), 1, 5, 0, 0, true);
        when(usuarioClientPort.consultarIdRestauranteDeUsuario(7)).thenReturn(20L);
        when(pedidoRepositoryPort.listarPorRestauranteYEstado(20L, EstadoPedidoEnum.LISTO, 1, 5))
                .thenReturn(esperado);

        PaginacionResultado<Pedido> resultado =
                consultarPedidoService.listarPedidosPorEstado(7, EstadoPedidoEnum.LISTO, 1, 5);

        assertSame(esperado, resultado);
        verify(pedidoRepositoryPort).listarPorRestauranteYEstado(20L, EstadoPedidoEnum.LISTO, 1, 5);
    }
}
