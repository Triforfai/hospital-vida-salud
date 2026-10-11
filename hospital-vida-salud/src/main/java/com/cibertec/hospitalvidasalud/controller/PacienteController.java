package com.cibertec.hospitalvidasalud.controller;

import com.cibertec.hospitalvidasalud.model.Paciente;
import com.cibertec.hospitalvidasalud.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Paciente> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Mono<Paciente> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Paciente>> registrar(
            @Valid @RequestBody Paciente paciente) {

        return service.registrar(paciente)
                .map(guardado -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(guardado));
    }
}
