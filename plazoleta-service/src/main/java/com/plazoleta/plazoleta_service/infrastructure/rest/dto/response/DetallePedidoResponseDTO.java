package com.plazoleta.plazoleta_service.infrastructure.rest.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePedidoResponseDTO {
    private Long idPedido;
    private Long idPlato;
    private Integer cantidad;
}
