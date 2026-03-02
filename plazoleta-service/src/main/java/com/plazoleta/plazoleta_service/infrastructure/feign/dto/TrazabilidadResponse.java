package com.plazoleta.plazoleta_service.infrastructure.feign.dto;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TrazabilidadResponse {
    private Long idPedido;
    private Long idRestaurante;
    private EstadoPedidoEnum estadoNuevo;
    private Integer idEmpleado;
    private Date fecha;
}
