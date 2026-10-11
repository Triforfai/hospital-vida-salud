
package com.cibertec.laboratorioservice.service;

import com.cibertec.laboratorioservice.model.SolicitudLaboratorio;
import com.cibertec.laboratorioservice.repository.SolicitudLaboratorioRepository;
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
public class LaboratorioService {

    private final SolicitudLaboratorioRepository repository;
    private final WebClient atencionesClient;

    public LaboratorioService(
            SolicitudLaboratorioRepository repository,
            WebClient.Builder webClientBuilder) {

        this.repository = repository;
        this.atencionesClient = webClientBuilder
                .baseUrl("http://ATENCIONES-SERVICE")
                .build();
    }

    public Mono<SolicitudLaboratorio> crear(SolicitudLaboratorio solicitud) {

        if (solicitud.getAtencionId() == null
                || solicitud.getAtencionId() <= 0) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El identificador de atencion no es valido"
            ));
        }

        if (solicitud.getExamen() == null
                || solicitud.getExamen().isBlank()) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El examen es obligatorio"
            ));
        }

        solicitud.setId(null);
        solicitud.setEstado("SOLICITADO");
        solicitud.setResultado(null);
        solicitud.setFechaSolicitud(LocalDateTime.now());

        return validarAtencion(solicitud.getAtencionId())
                .then(Mono.defer(() -> repository.save(solicitud)));
    }

    private Mono<Void> validarAtencion(Long atencionId) {

        return atencionesClient.get()
                .uri("/api/atenciones/{id}", atencionId)
                .retrieve()
                .bodyToMono(String.class)
                .then()
                .onErrorResume(
                        WebClientResponseException.NotFound.class,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "No existe la atencion con id " + atencionId
                        ))
                )
                .onErrorMap(
                        WebClientRequestException.class,
                        error -> new ResponseStatusException(
                                HttpStatus.SERVICE_UNAVAILABLE,
                                "No se pudo contactar con Atenciones"
                        )
                );
    }

    public Flux<SolicitudLaboratorio> listar() {
        return repository.findAll();
    }

    public Mono<SolicitudLaboratorio> buscarPorId(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Solicitud de laboratorio no encontrada"
                        )
                ));
    }
}
