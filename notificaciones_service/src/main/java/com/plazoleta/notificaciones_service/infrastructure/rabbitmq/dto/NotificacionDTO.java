package com.plazoleta.notificaciones_service.infrastructure.rabbitmq.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionDTO {
    private Long idPedido;
    private Integer idCliente;
    private String celularDestino;
    private String mensaje;
    private String pin;
}
