package com.mitocode.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "signo")
public class Signo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSigno;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false, foreignKey = @ForeignKey(name = "FK_signo_paciente"))
    private Paciente paciente;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "pulso")
    private Integer pulso;

    @Column(name = "ritmo_respiratorio")
    private String ritmoRespiratorio;


    public Integer getIdSigno() {
        return idSigno;
    }

    public void setIdSigno(Integer idSigno) {
        this.idSigno = idSigno;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getPulso() {
        return pulso;
    }

    public void setPulso(Integer pulso) {
        this.pulso = pulso;
    }

    public String getRitmoRespiratorio() {
        return ritmoRespiratorio;
    }

    public void setRitmoRespiratorio(String ritmoRespiratorio) {
        this.ritmoRespiratorio = ritmoRespiratorio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signo signo = (Signo) o;
        return Objects.equals(idSigno, signo.idSigno) &&
                Objects.equals(paciente, signo.paciente) &&
                Objects.equals(fecha, signo.fecha) &&
                Objects.equals(temperatura, signo.temperatura) &&
                Objects.equals(pulso, signo.pulso) &&
                Objects.equals(ritmoRespiratorio, signo.ritmoRespiratorio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSigno, paciente, fecha, temperatura, pulso, ritmoRespiratorio);
    }
}
