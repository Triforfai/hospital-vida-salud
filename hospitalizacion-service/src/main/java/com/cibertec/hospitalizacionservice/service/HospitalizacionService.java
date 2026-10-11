
package com.cibertec.hospitalizacionservice.service;

import com.cibertec.hospitalizacionservice.model.Hospitalizacion;
import com.cibertec.hospitalizacionservice.repository.HospitalizacionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class HospitalizacionService {

    private final HospitalizacionRepository repository;

    public HospitalizacionService(HospitalizacionRepository repository) {
        this.repository = repository;
    }

    public Mono<Hospitalizacion> ingresar(Hospitalizacion h) {

        if (h.getPacienteId() == null || h.getPacienteId() <= 0
                || h.getCamaId() == null || h.getCamaId().isBlank()
                || h.getMotivo() == null || h.getMotivo().isBlank()) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Paciente, cama y motivo son obligatorios"
            ));
        }

        h.setId(null);
        h.setEstado("INGRESADO");
        h.setFechaIngreso(LocalDateTime.now());
        h.setFechaAlta(null);

        return repository.existsByCamaIdAndEstado(
                        h.getCamaId(), "INGRESADO")
                .flatMap(ocupada -> {
                    if (ocupada) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "La cama ya tiene una hospitalizacion activa"
                        ));
                    }
                    return repository.save(h);
                })
                .onErrorMap(
                        DataIntegrityViolationException.class,
                        error -> new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "La cama ya tiene una hospitalizacion activa"
                        )
                );
    }

    public Flux<Hospitalizacion> listar() {
        return repository.findAll();
    }

    public Mono<Hospitalizacion> buscar(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospitalizacion no encontrada"
                )));
    }

    public Mono<Hospitalizacion> darAlta(Long id) {
        return buscar(id).flatMap(h -> {
            if (!"INGRESADO".equals(h.getEstado())) {
                return Mono.error(new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "La hospitalizacion no esta activa"
                ));
            }

            h.setEstado("ALTA");
            h.setFechaAlta(LocalDateTime.now());
            return repository.save(h);
        });
    }
}
