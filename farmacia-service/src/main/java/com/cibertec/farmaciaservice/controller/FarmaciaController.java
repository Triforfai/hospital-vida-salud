
package com.cibertec.farmaciaservice.controller;

import com.cibertec.farmaciaservice.model.Medicamento;
import com.cibertec.farmaciaservice.service.FarmaciaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/farmacia")
public class FarmaciaController {

    private final FarmaciaService service;

    public FarmaciaController(FarmaciaService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<Medicamento> crear(@RequestBody Medicamento medicamento) {
        return service.crear(medicamento);
    }

    @GetMapping
    public Flux<Medicamento> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Mono<Medicamento> buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping("/{id}/dispensar")
    public Mono<Medicamento> dispensar(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        return service.dispensar(id, cantidad);
    }
}
