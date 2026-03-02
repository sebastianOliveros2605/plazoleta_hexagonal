package com.plazoleta.trazabilidad_service.infrastructure.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReporteTiempoPedidoResponseDTO {

    private Long idPedido;
    private int totalEventos;
    private Date fechaPrimerEvento;
    private Date fechaUltimoEvento;
    private long duracionTotalSegundos;
    private List<TransicionTiempoResponseDTO> transiciones;
}
