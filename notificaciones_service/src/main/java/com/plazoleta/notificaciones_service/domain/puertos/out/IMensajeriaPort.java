package com.plazoleta.notificaciones_service.domain.puertos.out;

import com.plazoleta.notificaciones_service.domain.model.Notificacion;

public interface IMensajeriaPort {
    void enviarSms(Notificacion notificacion);
}
