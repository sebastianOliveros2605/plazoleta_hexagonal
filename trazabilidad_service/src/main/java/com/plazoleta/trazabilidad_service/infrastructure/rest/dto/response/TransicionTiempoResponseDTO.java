package com.plazoleta.trazabilidad_service.infrastructure.rest.dto.response;

import com.plazoleta.trazabilidad_service.domain.model.EstadoPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransicionTiempoResponseDTO {

    private EstadoPedidoEnum estadoAnterior;
    private EstadoPedidoEnum estadoNuevo;
    private long duracionSegundos;
}
