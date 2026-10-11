
package com.cibertec.notificacionesservice;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CitaNotificacionListener {

    @RabbitListener(queues = "notificaciones.cita.confirmada")
    public void recibirCitaConfirmada(String mensaje) {

        System.out.println("==================================");
        System.out.println("NOTIFICACION DE CITA RECIBIDA");
        System.out.println("Evento: " + mensaje);
        System.out.println("==================================");
    }
}
