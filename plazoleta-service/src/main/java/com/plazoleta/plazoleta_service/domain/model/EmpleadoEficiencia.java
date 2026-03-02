package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpleadoEficiencia {
    private Integer idEmpleado;
    private long totalPedidosCompletados;
    private long tiempoPromedioSegundos;
    private long tiempoMinimoSegundos;
    private long tiempoMaximoSegundos;
}
