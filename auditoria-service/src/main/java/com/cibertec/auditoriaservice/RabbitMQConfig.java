
package com.cibertec.auditoriaservice;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "hospital.exchange";
    public static final String QUEUE = "auditoria.eventos.cita";
    public static final String ROUTING_KEY = "cita.confirmada";

    @Bean
    public TopicExchange hospitalExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue colaAuditoria() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding bindingAuditoria(
            Queue colaAuditoria,
            TopicExchange hospitalExchange) {

        return BindingBuilder
                .bind(colaAuditoria)
                .to(hospitalExchange)
                .with(ROUTING_KEY);
    }
}
