package com.plazoleta.plazoleta_service.infrastructure.feign.dto;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrazabilidadRequest {

    private Long idPedido;
    private Long idRestaurante;
    private Integer idCliente;
    private String correoCliente;
    private EstadoPedidoEnum estadoAnterior;
    private EstadoPedidoEnum estadoNuevo;
    private Integer idEmpleado;
    private String correoEmpleado;
}
