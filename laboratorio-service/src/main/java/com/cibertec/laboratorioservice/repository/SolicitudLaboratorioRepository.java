
package com.cibertec.laboratorioservice.repository;

import com.cibertec.laboratorioservice.model.SolicitudLaboratorio;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SolicitudLaboratorioRepository
        extends ReactiveCrudRepository<SolicitudLaboratorio, Long> {
}
