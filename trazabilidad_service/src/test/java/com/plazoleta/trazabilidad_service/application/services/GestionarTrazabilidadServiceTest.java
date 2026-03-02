package com.plazoleta.trazabilidad_service.application.services;

import com.plazoleta.trazabilidad_service.domain.exception.TrazabilidadInvalidaException;
import com.plazoleta.trazabilidad_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.trazabilidad_service.domain.model.Trazabilidad;
import com.plazoleta.trazabilidad_service.domain.ports.out.ITrazabilidadRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestionarTrazabilidadServiceTest {

    @Mock
    private ITrazabilidadRepositoryPort trazabilidadRepositoryPort;

    @InjectMocks
    private GestionarTrazabilidadService gestionarTrazabilidadService;

    @Test
    void flujoFeliz_guardarYConsultarPorPedidoClienteYRestaurante() {
        Trazabilidad trazabilidad = baseTrazabilidad();
        trazabilidad.setFecha(null);

        List<Trazabilidad> trazasPedido = List.of(baseTrazabilidad());
        List<Trazabilidad> trazasCliente = List.of(baseTrazabilidad());
        List<Trazabilidad> trazasRestaurante = List.of(baseTrazabilidad());

        when(trazabilidadRepositoryPort.guardar(any(Trazabilidad.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(trazabilidadRepositoryPort.consultarPorPedido(1L)).thenReturn(trazasPedido);
        when(trazabilidadRepositoryPort.consultarPorCliente(55)).thenReturn(trazasCliente);
        when(trazabilidadRepositoryPort.consultarPorRestaurante(10L)).thenReturn(trazasRestaurante);

        Trazabilidad guardada = gestionarTrazabilidadService.guardar(trazabilidad);
        List<Trazabilidad> porPedido = gestionarTrazabilidadService.consultarPorPedido(1L);
        List<Trazabilidad> porCliente = gestionarTrazabilidadService.consultarPorCliente(55);
        List<Trazabilidad> porRestaurante = gestionarTrazabilidadService.consultarPorRestaurante(10L);

        assertNotNull(guardada.getFecha());
        assertSame(trazasPedido, porPedido);
        assertSame(trazasCliente, porCliente);
        assertSame(trazasRestaurante, porRestaurante);
        verify(trazabilidadRepositoryPort).guardar(any(Trazabilidad.class));
        verify(trazabilidadRepositoryPort).consultarPorPedido(1L);
        verify(trazabilidadRepositoryPort).consultarPorCliente(55);
        verify(trazabilidadRepositoryPort).consultarPorRestaurante(10L);
    }

    @Test
    void guardar_cuandoEstadoAnteriorEsIgualANuevo_debeFallarYNoPersistir() {
        Trazabilidad trazabilidad = baseTrazabilidad();
        trazabilidad.setEstadoAnterior(EstadoPedidoEnum.LISTO);
        trazabilidad.setEstadoNuevo(EstadoPedidoEnum.LISTO);

        TrazabilidadInvalidaException exception = assertThrows(TrazabilidadInvalidaException.class,
                () -> gestionarTrazabilidadService.guardar(trazabilidad));

        assertEquals("El estado anterior no puede ser igual al estado nuevo.", exception.getMessage());
        verify(trazabilidadRepositoryPort, never()).guardar(any());
    }

    @Test
    void guardar_cuandoEstadoNuevoEsPendienteConAnteriorInformado_debeFallarYNoPersistir() {
        Trazabilidad trazabilidad = baseTrazabilidad();
        trazabilidad.setEstadoAnterior(EstadoPedidoEnum.EN_PREPARACION);
        trazabilidad.setEstadoNuevo(EstadoPedidoEnum.PENDIENTE);

        TrazabilidadInvalidaException exception = assertThrows(TrazabilidadInvalidaException.class,
                () -> gestionarTrazabilidadService.guardar(trazabilidad));

        assertEquals("PENDIENTE solo puede usarse como primer estado del pedido.", exception.getMessage());
        verify(trazabilidadRepositoryPort, never()).guardar(any());
    }

    @Test
    void consultarPorCliente_cuandoIdEsInvalido_debeFallarYNoConsultar() {
        TrazabilidadInvalidaException exception = assertThrows(
                TrazabilidadInvalidaException.class,
                () -> gestionarTrazabilidadService.consultarPorCliente(0)
        );

        assertEquals("El id del cliente es obligatorio y debe ser positivo.", exception.getMessage());
        verify(trazabilidadRepositoryPort, never()).consultarPorCliente(any());
    }

    private Trazabilidad baseTrazabilidad() {
        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setIdPedido(1L);
        trazabilidad.setIdRestaurante(10L);
        trazabilidad.setIdCliente(55);
        trazabilidad.setEstadoAnterior(EstadoPedidoEnum.PENDIENTE);
        trazabilidad.setEstadoNuevo(EstadoPedidoEnum.EN_PREPARACION);
        trazabilidad.setFecha(new Date());
        return trazabilidad;
    }
}
