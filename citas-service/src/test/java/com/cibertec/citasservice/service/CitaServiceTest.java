package com.cibertec.citasservice.service;

import com.cibertec.citasservice.model.Cita;
import com.cibertec.citasservice.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CitaServiceTest {

    private CitaRepository repository;
    private CitaEventPublisher publisher;
    private CitaService service;

    @BeforeEach
    void configurar() {
        repository = mock(CitaRepository.class);
        publisher = mock(CitaEventPublisher.class);

        WebClient.Builder builder = mock(WebClient.Builder.class);
        WebClient client = mock(WebClient.class);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(client);

        service = new CitaService(repository, publisher, builder);
    }

    @Test
    void debeConfirmarUnaCitaPendiente() {
        Cita cita = nuevaCita("PENDIENTE");

        when(repository.findById(1L)).thenReturn(Mono.just(cita));
        when(repository.save(cita)).thenReturn(Mono.just(cita));
        when(publisher.publicarCitaConfirmada(1L, 1L))
                .thenReturn(Mono.empty());

        Cita resultado = service.confirmar(1L).block();

        assertNotNull(resultado);
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(publisher).publicarCitaConfirmada(1L, 1L);
    }

    @Test
    void noDebeConfirmarUnaCitaYaConfirmada() {
        Cita cita = nuevaCita("CONFIRMADA");

        when(repository.findById(1L)).thenReturn(Mono.just(cita));

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> service.confirmar(1L).block()
        );

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(repository, never()).save(any(Cita.class));
        verify(publisher, never())
                .publicarCitaConfirmada(anyLong(), anyLong());
    }

    @Test
    void debeInformarCuandoLaCitaNoExiste() {
        when(repository.findById(99L)).thenReturn(Mono.empty());

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> service.buscarPorId(99L).block()
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    private Cita nuevaCita(String estado) {
        Cita cita = new Cita();
        cita.setId(1L);
        cita.setPacienteId(1L);
        cita.setEspecialidad("Medicina General");
        cita.setEstado(estado);
        return cita;
    }
}
