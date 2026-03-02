package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Pedido {
    private Long id;
    private Integer idCliente;
    private Long idRestaurante;
    private EstadoPedidoEnum estado;
    private Date fechaCreacion;
    private Integer idEmpleado;
    private List<DetallePedido> detallePedido;
    private Date fechaEntrega;
    private String pinSeguridad;

}
