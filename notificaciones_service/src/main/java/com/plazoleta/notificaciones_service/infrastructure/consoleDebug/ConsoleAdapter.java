package com.plazoleta.notificaciones_service.infrastructure.consoleDebug;

import com.plazoleta.notificaciones_service.domain.model.Notificacion;
import com.plazoleta.notificaciones_service.domain.puertos.out.IMensajeriaPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
// Esto hace que este componente SOLO se active si la propiedad es 'console'
@ConditionalOnProperty(name = "notificaciones.provider", havingValue = "console", matchIfMissing = true)
public class ConsoleAdapter implements IMensajeriaPort {

    @Override
    public void enviarSms(Notificacion notificacion) {
        System.out.println("--- [DEBUG SMS MODE] ---");
        System.out.println("Para: " + notificacion.getCelularDestino());
        System.out.println("Mensaje: " + notificacion.getMensaje());
        System.out.println("PIN generado en Plazoleta: " + notificacion.getPin());
        System.out.println("-------------------------");
    }
}