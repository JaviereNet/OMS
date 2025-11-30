package es.educastur.oms.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_entradas")
public class HistorialEntrada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "estado", length = 50)
    private String estado; // guardamos nombre del enum

    @Column(name = "comentario", length = 250)
    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_historial")
    private Historial historial;

    public HistorialEntrada() {}

    public HistorialEntrada(LocalDateTime fechaHora, String estado, String comentario) {
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.comentario = comentario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Historial getHistorial() {
        return historial;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

    @Override
    public String toString() {
        return "HistorialEntrada{" + "id=" + id + ", fechaHora=" + fechaHora + ", estado='" + estado + '\'' + '}';
    }
}

