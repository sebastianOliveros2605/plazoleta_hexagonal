package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReporteEficienciaPedidos {
    private Long idRestaurante;
    private long totalPedidosCompletados;
    private long tiempoPromedioSegundos;
    private List<PedidoEficiencia> pedidos;
    private List<EmpleadoEficiencia> rankingEmpleados;
}
