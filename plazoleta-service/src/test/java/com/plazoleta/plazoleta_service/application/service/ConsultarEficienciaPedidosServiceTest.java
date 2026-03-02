package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.FiltroEficienciaPedidos;
import com.plazoleta.plazoleta_service.domain.model.ReporteEficienciaPedidos;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.model.TrazabilidadEvento;
import com.plazoleta.plazoleta_service.domain.ports.out.IPedidoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.ITrazabilidadClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarEficienciaPedidosServiceTest {

    @Mock
    private IRestauranteRepositoryPort restauranteRepositoryPort;

    @Mock
    private IPedidoRepositoryPort pedidoRepositoryPort;

    @Mock
    private ITrazabilidadClientPort trazabilidadClientPort;

    @InjectMocks
    private ConsultarEficienciaPedidosService consultarEficienciaPedidosService;

    @Test
    void consultar_debeCalcularResumenYRankingPorEmpleado() {
        when(restauranteRepositoryPort.buscarPorIdPropietario(7)).thenReturn(Optional.of(restaurante(20L)));
        when(pedidoRepositoryPort.consultarIdsFinalizadosPorRestaurante(20L)).thenReturn(List.of(1L, 2L));
        when(trazabilidadClientPort.consultarPorRestaurante(20L)).thenReturn(List.of(
                evento(1L, 20L, EstadoPedidoEnum.PENDIENTE, 4, 0),
                evento(1L, 20L, EstadoPedidoEnum.EN_PREPARACION, 4, 300),
                evento(1L, 20L, EstadoPedidoEnum.ENTREGADO, 4, 600),
                evento(2L, 20L, EstadoPedidoEnum.PENDIENTE, 9, 0),
                evento(2L, 20L, EstadoPedidoEnum.EN_PREPARACION, 9, 120),
                evento(2L, 20L, EstadoPedidoEnum.ENTREGADO, 9, 360)
        ));

        FiltroEficienciaPedidos filtro = new FiltroEficienciaPedidos();
        filtro.setIncluirDetalleTransiciones(true);

        ReporteEficienciaPedidos reporte = consultarEficienciaPedidosService.consultar(7, filtro);

        assertEquals(20L, reporte.getIdRestaurante());
        assertEquals(2, reporte.getTotalPedidosCompletados());
        assertEquals(480, reporte.getTiempoPromedioSegundos());
        assertEquals(2, reporte.getRankingEmpleados().size());
        assertEquals(9, reporte.getRankingEmpleados().get(0).getIdEmpleado());
    }

    @Test
    void consultar_conFiltroEmpleadoYSinDetalleDebeAplicarFiltro() {
        when(restauranteRepositoryPort.buscarPorIdPropietario(7)).thenReturn(Optional.of(restaurante(20L)));
        when(pedidoRepositoryPort.consultarIdsFinalizadosPorRestaurante(20L)).thenReturn(List.of(1L, 2L));
        when(trazabilidadClientPort.consultarPorRestaurante(20L)).thenReturn(List.of(
                evento(1L, 20L, EstadoPedidoEnum.PENDIENTE, 4, 0),
                evento(1L, 20L, EstadoPedidoEnum.ENTREGADO, 4, 120),
                evento(2L, 20L, EstadoPedidoEnum.PENDIENTE, 9, 0),
                evento(2L, 20L, EstadoPedidoEnum.ENTREGADO, 9, 300)
        ));

        FiltroEficienciaPedidos filtro = new FiltroEficienciaPedidos();
        filtro.setIdEmpleado(4);
        filtro.setIncluirDetalleTransiciones(false);

        ReporteEficienciaPedidos reporte = consultarEficienciaPedidosService.consultar(7, filtro);

        assertEquals(1, reporte.getPedidos().size());
        assertEquals(1, reporte.getRankingEmpleados().size());
        assertEquals(4, reporte.getRankingEmpleados().get(0).getIdEmpleado());
        assertEquals(0, reporte.getPedidos().get(0).getTransiciones().size());
    }

    @Test
    void consultar_debeRecuperarEventosLegacyDesdeConsultaPorPedido() {
        when(restauranteRepositoryPort.buscarPorIdPropietario(7)).thenReturn(Optional.of(restaurante(20L)));
        when(pedidoRepositoryPort.consultarIdsFinalizadosPorRestaurante(20L)).thenReturn(List.of(1L));
        when(trazabilidadClientPort.consultarPorRestaurante(20L)).thenReturn(List.of());
        when(trazabilidadClientPort.consultarPorPedido(1L)).thenReturn(List.of(
                evento(1L, null, EstadoPedidoEnum.PENDIENTE, 4, 0),
                evento(1L, null, EstadoPedidoEnum.ENTREGADO, 4, 120)
        ));

        FiltroEficienciaPedidos filtro = new FiltroEficienciaPedidos();
        filtro.setIncluirDetalleTransiciones(true);

        ReporteEficienciaPedidos reporte = consultarEficienciaPedidosService.consultar(7, filtro);

        assertEquals(1, reporte.getTotalPedidosCompletados());
        assertEquals(120, reporte.getTiempoPromedioSegundos());
    }

    private TrazabilidadEvento evento(Long idPedido, Long idRestaurante, EstadoPedidoEnum estadoNuevo, Integer idEmpleado, int segundos) {
        TrazabilidadEvento evento = new TrazabilidadEvento();
        evento.setIdPedido(idPedido);
        evento.setIdRestaurante(idRestaurante);
        evento.setEstadoNuevo(estadoNuevo);
        evento.setIdEmpleado(idEmpleado);
        evento.setFecha(new Date(1_700_000_000_000L + (segundos * 1000L)));
        return evento;
    }

    private Restaurante restaurante(Long id) {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        return restaurante;
    }
}
