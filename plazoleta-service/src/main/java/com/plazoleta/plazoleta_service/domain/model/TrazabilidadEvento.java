package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TrazabilidadEvento {
    private Long idPedido;
    private Long idRestaurante;
    private EstadoPedidoEnum estadoNuevo;
    private Integer idEmpleado;
    private Date fecha;
}
