
package com.cibertec.hospitalizacionservice.controller;

import com.cibertec.hospitalizacionservice.model.Hospitalizacion;
import com.cibertec.hospitalizacionservice.service.HospitalizacionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hospitalizaciones")
public class HospitalizacionController {

    private final HospitalizacionService service;

    public HospitalizacionController(HospitalizacionService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<Hospitalizacion> ingresar(
            @RequestBody Hospitalizacion hospitalizacion) {
        return service.ingresar(hospitalizacion);
    }

    @GetMapping
    public Flux<Hospitalizacion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Mono<Hospitalizacion> buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping("/{id}/alta")
    public Mono<Hospitalizacion> darAlta(@PathVariable Long id) {
        return service.darAlta(id);
    }
}