package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PedidoEficiencia {
    private Long idPedido;
    private Integer idEmpleado;
    private EstadoPedidoEnum estadoFinal;
    private Date fechaInicio;
    private Date fechaFin;
    private long duracionTotalSegundos;
    private int totalEventos;
    private List<TransicionEficiencia> transiciones;
}
