
package com.cibertec.citasservice.repository;

import com.cibertec.citasservice.model.Cita;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CitaRepository extends ReactiveCrudRepository<Cita, Long> {
}
