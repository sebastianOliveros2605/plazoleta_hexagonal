package com.plazoleta.notificaciones_service.domain.puertos.in;

import com.plazoleta.notificaciones_service.domain.model.Notificacion;

public interface IEventoEnviaNotificacionUseCase {
    void enviarSms(Notificacion notificacion);
}
