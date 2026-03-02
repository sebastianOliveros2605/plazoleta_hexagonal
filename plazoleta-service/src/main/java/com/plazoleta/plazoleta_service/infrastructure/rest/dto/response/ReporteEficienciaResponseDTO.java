package com.plazoleta.plazoleta_service.infrastructure.rest.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReporteEficienciaResponseDTO {
    private Long idRestaurante;
    private long totalPedidosCompletados;
    private long tiempoPromedioSegundos;
    private List<EficienciaPedidoResponseDTO> pedidos;
    private List<EficienciaEmpleadoResponseDTO> rankingEmpleados;
}
