package com.cibertec.notificacionesservice;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "hospital.exchange";
    public static final String QUEUE = "notificaciones.cita.confirmada";
    public static final String ROUTING_KEY = "cita.confirmada";

    @Bean
    public TopicExchange hospitalExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue colaNotificaciones() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding bindingCitaConfirmada(
            Queue colaNotificaciones,
            TopicExchange hospitalExchange) {
        return BindingBuilder
                .bind(colaNotificaciones)
                .to(hospitalExchange)
                .with(ROUTING_KEY);
    }
}