
package com.cibertec.atencionesservice.controller;

import com.cibertec.atencionesservice.model.Atencion;
import com.cibertec.atencionesservice.service.AtencionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    private final AtencionService atencionService;

    public AtencionController(AtencionService atencionService) {
        this.atencionService = atencionService;
    }

    @PostMapping
    public Mono<Atencion> crear(@RequestBody Atencion atencion) {
        return atencionService.crear(atencion);
    }

    @GetMapping
    public Flux<Atencion> listar() {
        return atencionService.listar();
    }

    @GetMapping("/{id}")
    public Mono<Atencion> buscarPorId(@PathVariable Long id) {
        return atencionService.buscarPorId(id);
    }
}
