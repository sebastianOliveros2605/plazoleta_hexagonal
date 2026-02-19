package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePedido {
    private Long idPedido;
    private Long idPlato;
    private Integer cantidad;
}
