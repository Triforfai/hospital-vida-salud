
package com.cibertec.laboratorioservice.controller;

import com.cibertec.laboratorioservice.model.SolicitudLaboratorio;
import com.cibertec.laboratorioservice.service.LaboratorioService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/laboratorio")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @PostMapping
    public Mono<SolicitudLaboratorio> crear(
            @RequestBody SolicitudLaboratorio solicitud) {
        return laboratorioService.crear(solicitud);
    }

    @GetMapping
    public Flux<SolicitudLaboratorio> listar() {
        return laboratorioService.listar();
    }

    @GetMapping("/{id}")
    public Mono<SolicitudLaboratorio> buscarPorId(@PathVariable Long id) {
        return laboratorioService.buscarPorId(id);
    }
}
