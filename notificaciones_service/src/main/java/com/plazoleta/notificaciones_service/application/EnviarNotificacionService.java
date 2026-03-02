package com.plazoleta.notificaciones_service.application;

import com.plazoleta.notificaciones_service.domain.constants.NotificacionDomainConstants;
import com.plazoleta.notificaciones_service.domain.model.Notificacion;
import com.plazoleta.notificaciones_service.domain.puertos.in.IEventoEnviaNotificacionUseCase;
import com.plazoleta.notificaciones_service.domain.puertos.out.IMensajeriaPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnviarNotificacionService implements IEventoEnviaNotificacionUseCase {
    private final IMensajeriaPort mensajeriaPort;
    @Override
    public void enviarSms(Notificacion notificacion) {
        if((notificacion.getCelularDestino() == null || notificacion.getCelularDestino().trim().isEmpty())
            || (notificacion.getPin() == null || notificacion.getPin().trim().isEmpty())){
            throw new IllegalArgumentException(NotificacionDomainConstants.MENSAJE_NOTIFICACION_INVALIDA);
        }

        mensajeriaPort.enviarSms(notificacion);

    }
}
