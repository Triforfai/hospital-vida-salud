
package com.cibertec.citasservice.controller;

import com.cibertec.citasservice.model.Cita;
import com.cibertec.citasservice.service.CitaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public Mono<Cita> crear(@RequestBody Cita cita) {
        return citaService.crear(cita);
    }

    @GetMapping
    public Flux<Cita> listar() {
        return citaService.listar();
    }

    @GetMapping("/{id}")
    public Mono<Cita> buscarPorId(@PathVariable Long id) {
        return citaService.buscarPorId(id);
    }

    @PostMapping("/{id}/confirmar")
    public Mono<Cita> confirmar(@PathVariable Long id) {
        return citaService.confirmar(id);
    }
}