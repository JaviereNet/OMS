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

//--------------------------------------------------------
// Autor: Refactor automático
// Fecha: 2025-11-28
// Descripción: Entidad Stock para gestionar cantidades por producto/ubicación.
//--------------------------------------------------------

@Entity
@Table(name = "stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "lote", length = 100)
    private String lote;

    @Column(name = "unidad", length = 20)
    private String unidad;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    public Stock() {
    }

    public Stock(Producto producto, Ubicacion ubicacion, int cantidad, String lote, String unidad) {
        this.producto = producto;
        this.ubicacion = ubicacion;
        this.cantidad = cantidad;
        this.lote = lote;
        this.unidad = unidad;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    @Override
    public String toString() {
        return "Stock{" + "id=" + id + ", producto=" + (producto != null ? producto.getCodigo() : null) + ", ubicacion=" + (ubicacion != null ? ubicacion.getIdUbicacion() : null) + ", cantidad=" + cantidad + ", lote='" + lote + '\'' + ", unidad='" + unidad + '\'' + '}';
    }
}

