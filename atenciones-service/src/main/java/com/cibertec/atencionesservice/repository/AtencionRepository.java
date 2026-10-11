
package com.cibertec.atencionesservice.repository;

import com.cibertec.atencionesservice.model.Atencion;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AtencionRepository
        extends ReactiveCrudRepository<Atencion, Long> {
}
