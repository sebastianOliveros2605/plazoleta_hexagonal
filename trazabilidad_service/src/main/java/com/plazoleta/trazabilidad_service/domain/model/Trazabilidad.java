package com.plazoleta.trazabilidad_service.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Trazabilidad {
    private String id;
    private Long idPedido;
    private Long idRestaurante;
    private Integer idCliente;
    private String correoCliente;
    private Date fecha;
    private EstadoPedidoEnum estadoAnterior;
    private EstadoPedidoEnum estadoNuevo;
    private Integer idEmpleado;
    private String correoEmpleado;

}
