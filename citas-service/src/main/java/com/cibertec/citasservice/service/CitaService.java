
package com.cibertec.citasservice.service;

import com.cibertec.citasservice.model.Cita;
import com.cibertec.citasservice.repository.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final CitaEventPublisher citaEventPublisher;
    private final WebClient pacientesClient;

    public CitaService(
            CitaRepository citaRepository,
            CitaEventPublisher citaEventPublisher,
            WebClient.Builder webClientBuilder) {

        this.citaRepository = citaRepository;
        this.citaEventPublisher = citaEventPublisher;
        this.pacientesClient = webClientBuilder
                .baseUrl("http://PACIENTES-SERVICE")
                .build();
    }

    public Mono<Cita> crear(Cita cita) {

        if (cita.getPacienteId() == null || cita.getPacienteId() <= 0) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El identificador del paciente no es valido"
            ));
        }

        if (cita.getEspecialidad() == null
                || cita.getEspecialidad().isBlank()) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La especialidad es obligatoria"
            ));
        }

        if (cita.getFechaHora() == null
                || !cita.getFechaHora().isAfter(LocalDateTime.now())) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de la cita debe ser futura"
            ));
        }

        cita.setId(null);
        cita.setEstado("PENDIENTE");

        return validarPaciente(cita.getPacienteId())
                .then(Mono.defer(() -> citaRepository.save(cita)));
    }

    private Mono<Void> validarPaciente(Long pacienteId) {

        return pacientesClient.get()
                .uri("/api/pacientes/{id}", pacienteId)
                .retrieve()
                .bodyToMono(String.class)
                .then()
                .onErrorResume(
                        WebClientResponseException.NotFound.class,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "No existe el paciente con id " + pacienteId
                        ))
                )
                .onErrorMap(
                        WebClientRequestException.class,
                        error -> new ResponseStatusException(
                                HttpStatus.SERVICE_UNAVAILABLE,
                                "No se pudo contactar con el servicio de Pacientes"
                        )
                );
    }

    public Flux<Cita> listar() {
        return citaRepository.findAll();
    }

    public Mono<Cita> buscarPorId(Long id) {
        return citaRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cita no encontrada"
                        )
                ));
    }

    public Mono<Cita> confirmar(Long id) {
        return citaRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cita no encontrada"
                        )
                ))
                .flatMap(cita -> {

                    if (!"PENDIENTE".equals(cita.getEstado())) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Solo se pueden confirmar citas pendientes"
                        ));
                    }

                    cita.setEstado("CONFIRMADA");

                    return citaRepository.save(cita)
                            .flatMap(citaGuardada ->
                                    citaEventPublisher.publicarCitaConfirmada(
                                            citaGuardada.getId(),
                                            citaGuardada.getPacienteId()
                                    ).thenReturn(citaGuardada)
                            );
                });
    }
}
