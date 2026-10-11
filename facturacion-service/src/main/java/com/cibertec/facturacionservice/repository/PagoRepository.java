
package com.cibertec.facturacionservice.repository;

import com.cibertec.facturacionservice.model.Pago;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PagoRepository extends ReactiveCrudRepository<Pago, Long> {

    Mono<Boolean> existsByNumeroTransaccion(String numeroTransaccion);
}
