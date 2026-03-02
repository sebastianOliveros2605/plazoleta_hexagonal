package com.plazoleta.plazoleta_service.infrastructure.rabbitmq.adapter;

import com.plazoleta.plazoleta_service.domain.ports.out.INotificacionEventPort;
import com.plazoleta.plazoleta_service.infrastructure.rabbitmq.config.RabbitMqConstants;
import com.plazoleta.plazoleta_service.infrastructure.rabbitmq.dto.NotificacionEventDTO;
import com.plazoleta.plazoleta_service.infrastructure.rabbitmq.mapper.NotificacionEventRabbitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificacionEventRabbitAdapter implements INotificacionEventPort {

    private final RabbitTemplate rabbitTemplate;
    private final NotificacionEventRabbitMapper notificacionEventRabbitMapper;

    @Override
    public void notificarPedidoListo(Long idPedido, Integer idCliente, String celularDestino, String mensaje, String pin) {
        NotificacionEventDTO payload = notificacionEventRabbitMapper.toDto(
                idPedido,
                idCliente,
                celularDestino,
                mensaje,
                pin);

        rabbitTemplate.convertAndSend(
                RabbitMqConstants.EXCHANGE_NOTIFICACIONES,
                RabbitMqConstants.ROUTING_KEY_PEDIDO_LISTO,
                payload);
    }
}
