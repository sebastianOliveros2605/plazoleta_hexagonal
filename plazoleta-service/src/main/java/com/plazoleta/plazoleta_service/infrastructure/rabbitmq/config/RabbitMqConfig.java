package com.plazoleta.plazoleta_service.infrastructure.rabbitmq.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Jackson2JsonMessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter rabbitMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(rabbitMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange notificacionesExchange() {
        return new DirectExchange(RabbitMqConstants.EXCHANGE_NOTIFICACIONES, true, false);
    }

    @Bean
    public Queue pedidoListoQueue() {
        return new Queue(RabbitMqConstants.QUEUE_PEDIDO_LISTO, true);
    }

    @Bean
    public Binding pedidoListoBinding(Queue pedidoListoQueue, DirectExchange notificacionesExchange) {
        return BindingBuilder.bind(pedidoListoQueue)
                .to(notificacionesExchange)
                .with(RabbitMqConstants.ROUTING_KEY_PEDIDO_LISTO);
    }
}
