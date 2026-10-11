
package com.cibertec.farmaciaservice.service;

import com.cibertec.farmaciaservice.model.Medicamento;
import com.cibertec.farmaciaservice.repository.MedicamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class FarmaciaService {

    private final MedicamentoRepository repository;

    public FarmaciaService(MedicamentoRepository repository) {
        this.repository = repository;
    }

    public Mono<Medicamento> crear(Medicamento medicamento) {
        if (medicamento.getNombre() == null
                || medicamento.getNombre().isBlank()
                || medicamento.getStock() == null
                || medicamento.getStock() < 0
                || medicamento.getPrecio() == null
                || medicamento.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nombre, stock y precio son obligatorios y deben ser validos"
            ));
        }

        medicamento.setId(null);
        return repository.save(medicamento);
    }

    public Flux<Medicamento> listar() {
        return repository.findAll();
    }

    public Mono<Medicamento> buscar(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Medicamento no encontrado"
                )));
    }

    public Mono<Medicamento> dispensar(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor que cero"
            ));
        }

        return buscar(id).flatMap(medicamento -> {
            if (medicamento.getStock() < cantidad) {
                return Mono.error(new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock insuficiente"
                ));
            }

            return repository.descontarStock(id, cantidad)
                    .flatMap(filas -> {
                        if (filas == null || filas == 0) {
                            return Mono.error(new ResponseStatusException(
                                    HttpStatus.CONFLICT,
                                    "Stock insuficiente; vuelva a consultar"
                            ));
                        }
                        return buscar(id);
                    });
        });
    }
}
