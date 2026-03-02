package com.plazoleta.notificaciones_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notificacion {

    private String celularDestino;
    private String mensaje;
    private String pin;

}
