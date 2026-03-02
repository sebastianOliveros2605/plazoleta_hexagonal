package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FiltroEficienciaPedidos {
    private Long idPedido;
    private Integer idEmpleado;
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean incluirDetalleTransiciones;
}
