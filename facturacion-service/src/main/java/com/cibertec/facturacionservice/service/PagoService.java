
package com.cibertec.facturacionservice.service;

import com.cibertec.facturacionservice.model.Pago;
import com.cibertec.facturacionservice.repository.PagoRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final WebClient webClient;

    public PagoService(
            PagoRepository pagoRepository,
            WebClient.Builder webClientBuilder) {

        this.pagoRepository = pagoRepository;
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8082")
                .build();
    }

    public Mono<Pago> registrar(Pago pago) {

        if (pago.getCitaId() == null || pago.getCitaId() <= 0) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El ID de la cita no es válido"));
        }

        if (pago.getMonto() == null
                || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El monto debe ser mayor que cero"));
        }

        if (pago.getNumeroTransaccion() == null
                || pago.getNumeroTransaccion().isBlank()) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El número de transacción es obligatorio"));
        }

        return webClient.get()
                .uri("/api/citas/{id}", pago.getCitaId())
                .retrieve()
                .bodyToMono(Object.class)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró la cita")))
                .onErrorMap(
                        WebClientResponseException.NotFound.class,
                        ex -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "La cita no existe"))
                .then(pagoRepository.existsByNumeroTransaccion(
                        pago.getNumeroTransaccion()))
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "El número de transacción ya existe"));
                    }

                    pago.setId(null);
                    pago.setEstado("PAGADO");
                    pago.setFechaPago(LocalDateTime.now());

                    return pagoRepository.save(pago);
                })
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "El número de transacción ya existe"));
    }

    public Flux<Pago> listar() {
        return pagoRepository.findAll();
    }

    public Mono<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pago no encontrado")));
    }
}
