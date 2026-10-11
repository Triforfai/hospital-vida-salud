
package com.cibertec.facturacionservice.controller;

import com.cibertec.facturacionservice.model.Pago;
import com.cibertec.facturacionservice.service.PagoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/facturacion")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public Mono<Pago> registrar(@RequestBody Pago pago) {
        return pagoService.registrar(pago);
    }

    @GetMapping
    public Flux<Pago> listar() {
        return pagoService.listar();
    }

    @GetMapping("/{id}")
    public Mono<Pago> buscarPorId(@PathVariable Long id) {
        return pagoService.buscarPorId(id);
    }
}
