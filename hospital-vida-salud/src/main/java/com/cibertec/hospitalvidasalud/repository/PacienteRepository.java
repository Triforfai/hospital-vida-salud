package com.cibertec.hospitalvidasalud.repository;

import com.cibertec.hospitalvidasalud.model.Paciente;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PacienteRepository
        extends ReactiveCrudRepository<Paciente, Long> {

    Mono<Boolean> existsByDni(String dni);
}