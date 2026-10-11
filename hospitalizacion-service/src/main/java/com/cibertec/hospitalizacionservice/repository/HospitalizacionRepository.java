
package com.cibertec.hospitalizacionservice.repository;

import com.cibertec.hospitalizacionservice.model.Hospitalizacion;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface HospitalizacionRepository
        extends ReactiveCrudRepository<Hospitalizacion, Long> {

    Mono<Boolean> existsByCamaIdAndEstado(
            String camaId, String estado);
}