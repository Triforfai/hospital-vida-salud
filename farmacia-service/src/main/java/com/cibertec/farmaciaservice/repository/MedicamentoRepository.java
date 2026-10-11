
package com.cibertec.farmaciaservice.repository;

import com.cibertec.farmaciaservice.model.Medicamento;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MedicamentoRepository
        extends ReactiveCrudRepository<Medicamento, Long> {

    @Modifying
    @Query("""
        UPDATE medicamento
        SET stock = stock - :cantidad
        WHERE id = :id AND stock >= :cantidad
        """)
    Mono<Integer> descontarStock(
            @Param("id") Long id,
            @Param("cantidad") Integer cantidad);
}