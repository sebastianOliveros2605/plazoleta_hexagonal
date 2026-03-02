package com.plazoleta.plazoleta_service.domain.ports.out;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.TrazabilidadEvento;

import java.util.List;

public interface ITrazabilidadClientPort {

    void registrarCambioEstado(
            Long idPedido,
            Long idRestaurante,
            Integer idCliente,
            String correoCliente,
            EstadoPedidoEnum estadoAnterior,
            EstadoPedidoEnum estadoNuevo,
            Integer idEmpleado,
            String correoEmpleado);

    List<TrazabilidadEvento> consultarPorRestaurante(Long idRestaurante);

    List<TrazabilidadEvento> consultarPorPedido(Long idPedido);
}
