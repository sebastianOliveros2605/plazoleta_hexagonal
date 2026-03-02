package com.plazoleta.plazoleta_service.infrastructure.feign.adapter;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.TrazabilidadEvento;
import com.plazoleta.plazoleta_service.domain.ports.out.ITrazabilidadClientPort;
import com.plazoleta.plazoleta_service.infrastructure.feign.client.ITrazabilidadFeignClient;
import com.plazoleta.plazoleta_service.infrastructure.feign.dto.TrazabilidadRequest;
import com.plazoleta.plazoleta_service.infrastructure.feign.mapper.TrazabilidadFeignMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrazabilidadClientAdapter implements ITrazabilidadClientPort {

    private final ITrazabilidadFeignClient trazabilidadFeignClient;
    private final TrazabilidadFeignMapper trazabilidadFeignMapper;

    @Override
    public void registrarCambioEstado(
            Long idPedido,
            Long idRestaurante,
            Integer idCliente,
            String correoCliente,
            EstadoPedidoEnum estadoAnterior,
            EstadoPedidoEnum estadoNuevo,
            Integer idEmpleado,
            String correoEmpleado) {

        TrazabilidadRequest request = new TrazabilidadRequest();
        request.setIdPedido(idPedido);
        request.setIdRestaurante(idRestaurante);
        request.setIdCliente(idCliente);
        request.setCorreoCliente(correoCliente);
        request.setEstadoAnterior(estadoAnterior);
        request.setEstadoNuevo(estadoNuevo);
        request.setIdEmpleado(idEmpleado);
        request.setCorreoEmpleado(correoEmpleado);

        trazabilidadFeignClient.registrar(request);
    }

    @Override
    public List<TrazabilidadEvento> consultarPorRestaurante(Long idRestaurante) {
        return trazabilidadFeignMapper.toDomainList(
                trazabilidadFeignClient.consultarPorRestaurante(idRestaurante));
    }

    @Override
    public List<TrazabilidadEvento> consultarPorPedido(Long idPedido) {
        return trazabilidadFeignMapper.toDomainList(
                trazabilidadFeignClient.consultarPorPedido(idPedido));
    }
}
