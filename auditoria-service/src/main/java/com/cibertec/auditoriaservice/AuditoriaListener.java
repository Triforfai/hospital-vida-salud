
package com.cibertec.auditoriaservice;

import com.cibertec.auditoriaservice.model.EventoAuditoria;
import com.cibertec.auditoriaservice.repository.EventoAuditoriaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditoriaListener {

    private final EventoAuditoriaRepository repository;

    public AuditoriaListener(EventoAuditoriaRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "auditoria.eventos.cita")
    public void registrarEvento(String mensaje) {

        EventoAuditoria evento = new EventoAuditoria();

        evento.setTipoEvento(
                mensaje.contains("\"tipo\":\"CitaConfirmada\"")
                        ? "CitaConfirmada"
                        : "EVENTO_CITA"
        );

        evento.setDetalle(mensaje);
        evento.setFechaRegistro(LocalDateTime.now());

        EventoAuditoria guardado = repository.save(evento).block();

        System.out.println("==================================");
        System.out.println("EVENTO GUARDADO EN AUDITORIA");
        System.out.println("ID del registro: " + guardado.getId());
        System.out.println("Detalle: " + guardado.getDetalle());
        System.out.println("==================================");
    }
}
