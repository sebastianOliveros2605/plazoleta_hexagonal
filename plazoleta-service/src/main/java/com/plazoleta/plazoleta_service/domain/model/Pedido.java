package com.plazoleta.plazoleta_service.domain.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pedido {
    private Long id;
    private Long clienteId;
    private Long restauranteId;
    private EstadoPedidoEnum estado;
    private Date fechaCreacion;
    private Long empleadoIdM;
    private String codigoSeguridad;
    private DetallePedido detallePedido;

}
