
package com.cibertec.atencionesservice.service;

import com.cibertec.atencionesservice.model.Atencion;
import com.cibertec.atencionesservice.repository.AtencionRepository;
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
public class AtencionService {

    private final AtencionRepository repository;
    private final WebClient citasClient;

    public AtencionService(
            AtencionRepository repository,
            WebClient.Builder webClientBuilder) {

        this.repository = repository;
        this.citasClient = webClientBuilder
                .baseUrl("http://CITAS-SERVICE")
                .build();
    }

    public Mono<Atencion> crear(Atencion atencion) {

        if (atencion.getCitaId() == null
                || atencion.getCitaId() <= 0) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El identificador de la cita no es valido"
            ));
        }

        if (atencion.getDiagnostico() == null
                || atencion.getDiagnostico().isBlank()) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El diagnostico es obligatorio"
            ));
        }

        atencion.setId(null);
        atencion.setFechaAtencion(LocalDateTime.now());

        return validarCita(atencion.getCitaId())
                .then(Mono.defer(() -> repository.save(atencion)));
    }

    private Mono<Void> validarCita(Long citaId) {
        return citasClient.get()
                .uri("/api/citas/{id}", citaId)
                .retrieve()
                .bodyToMono(String.class)
                .then()
                .onErrorResume(
                        WebClientResponseException.NotFound.class,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "No existe la cita con id " + citaId
                        ))
                )
                .onErrorMap(
                        WebClientRequestException.class,
                        error -> new ResponseStatusException(
                                HttpStatus.SERVICE_UNAVAILABLE,
                                "No se pudo contactar con el servicio de Citas"
                        )
                );
    }

    public Flux<Atencion> listar() {
        return repository.findAll();
    }

    public Mono<Atencion> buscarPorId(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Atencion no encontrada"
                        )
                ));
    }
}
