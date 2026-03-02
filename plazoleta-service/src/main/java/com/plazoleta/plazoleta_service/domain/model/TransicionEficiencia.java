package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransicionEficiencia {
    private EstadoPedidoEnum estadoDesde;
    private EstadoPedidoEnum estadoHasta;
    private long duracionSegundos;
}
