package com.plazoleta.notificaciones_service.infrastructure.twillio.adapter;

import com.plazoleta.notificaciones_service.domain.model.Notificacion;
import com.plazoleta.notificaciones_service.domain.puertos.out.IMensajeriaPort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "notificaciones.provider", havingValue = "twilio")
public class TwillioAdapter implements IMensajeriaPort {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String fromNumber;

    @Override
    public void enviarSms(Notificacion notificacion) {
        String numeroDestino = notificacion.getCelularDestino();
        if (!numeroDestino.startsWith("+")) {
            numeroDestino = "+" + numeroDestino;
        }
        Twilio.init(accountSid, authToken);
        Message.creator(
                new PhoneNumber(numeroDestino),
                new PhoneNumber(fromNumber),
                notificacion.getMensaje()
        ).create();
    }
}
