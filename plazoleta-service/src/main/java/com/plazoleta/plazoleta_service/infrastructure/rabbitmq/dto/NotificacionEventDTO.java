package com.plazoleta.plazoleta_service.infrastructure.rabbitmq.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionEventDTO {

    private Long idPedido;
    private Integer idCliente;
    private String celularDestino;
    private String mensaje;
    private String pin;
}
