package com.plazoleta.notificaciones_service.infrastructure.rabbitmq.adapter;

import com.plazoleta.notificaciones_service.domain.puertos.in.IEventoEnviaNotificacionUseCase;
import com.plazoleta.notificaciones_service.infrastructure.rabbitmq.config.RabbitMqConstants;
import com.plazoleta.notificaciones_service.infrastructure.rabbitmq.dto.NotificacionDTO;
import com.plazoleta.notificaciones_service.infrastructure.rabbitmq.mapper.NotificacionEventMapper;
import com.twilio.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventListenerAdapter {

    private final IEventoEnviaNotificacionUseCase enviaNotificacionUseCase;
    private final NotificacionEventMapper notificacionEventMapper;

    @RabbitListener(queues = RabbitMqConstants.QUEUE_PEDIDO_LISTO)
    public void onPedidoListo(NotificacionDTO payload) {
        log.info("Evento recibido de pedido listo. idCliente={}, celular={}", payload.getIdCliente(), payload.getCelularDestino());
        try {
            enviaNotificacionUseCase.enviarSms(notificacionEventMapper.toModel(payload));
        } catch (ApiException exception) {
            log.error("Twilio rechazo el envio para pedido {}. Codigo={}, mensaje={}",
                    payload.getIdPedido(), exception.getCode(), exception.getMessage());
            throw new AmqpRejectAndDontRequeueException("Fallo no recuperable enviando SMS con Twilio", exception);
        }
    }
}
