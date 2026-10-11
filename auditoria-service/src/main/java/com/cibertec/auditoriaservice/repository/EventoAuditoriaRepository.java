
package com.cibertec.auditoriaservice.repository;

import com.cibertec.auditoriaservice.model.EventoAuditoria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EventoAuditoriaRepository
        extends ReactiveCrudRepository<EventoAuditoria, Long> {
}
