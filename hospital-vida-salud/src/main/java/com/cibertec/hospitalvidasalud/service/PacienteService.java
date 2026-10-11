package com.cibertec.hospitalvidasalud.service;

import com.cibertec.hospitalvidasalud.model.Paciente;
import com.cibertec.hospitalvidasalud.repository.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public Flux<Paciente> listar() {
        return repository.findAll();
    }

    public Mono<Paciente> buscarPorId(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Paciente no encontrado"
                        )
                ));
    }

    public Mono<Paciente> registrar(Paciente paciente) {
        paciente.setId(null);

        if (paciente.getActivo() == null) {
            paciente.setActivo(true);
        }

        if (paciente.getCreatedAt() == null) {
            paciente.setCreatedAt(LocalDateTime.now());
        }

        return repository.existsByDni(paciente.getDni())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Ya existe un paciente con ese DNI"
                                )
                        );
                    }

                    return repository.save(paciente);
                });
    }
}
