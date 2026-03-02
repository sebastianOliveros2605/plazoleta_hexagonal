package com.plazoleta.plazoleta_service.infrastructure.rest.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EficienciaEmpleadoResponseDTO {
    private Integer idEmpleado;
    private long totalPedidosCompletados;
    private long tiempoPromedioSegundos;
    private long tiempoMinimoSegundos;
    private long tiempoMaximoSegundos;
}
