
package com.cibertec.citasservice.service;

import com.cibertec.citasservice.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class CitaEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public CitaEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Void> publicarCitaConfirmada(
            Long citaId, Long pacienteId) {

        return Mono.<Void>fromRunnable(() -> {
            String mensaje = "{\"tipo\":\"CitaConfirmada\","
                    + "\"citaId\":" + citaId + ","
                    + "\"pacienteId\":" + pacienteId + ","
                    + "\"estado\":\"CONFIRMADA\"}";

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    mensaje
            );
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
