
package com.cibertec.hospitalizacionservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("hospitalizacion")
public class Hospitalizacion {

    @Id
    private Long id;

    @Column("paciente_id")
    private Long pacienteId;

    @Column("cama_id")
    private String camaId;

    private String motivo;
    private String estado;

    @Column("fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @Column("fecha_alta")
    private LocalDateTime fechaAlta;

    public Hospitalizacion() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getCamaId() { return camaId; }
    public void setCamaId(String camaId) { this.camaId = camaId; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
}
