package com.plazoleta.notificaciones_service.infrastructure.rabbitmq.config;

public final class RabbitMqConstants {

    public static final String EXCHANGE_NOTIFICACIONES = "plazoleta.notificaciones.exchange";
    public static final String QUEUE_PEDIDO_LISTO = "plazoleta.notificaciones.pedido-listo.queue";
    public static final String ROUTING_KEY_PEDIDO_LISTO = "pedido.listo";

    private RabbitMqConstants() {
    }
}
