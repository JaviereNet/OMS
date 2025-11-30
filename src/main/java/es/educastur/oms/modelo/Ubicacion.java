package es.educastur.oms.modelo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2025-01-16 (refactor 2025-11-28)
//Descripción: Clase VO de ubicación (simplificada para almacén).
//--------------------------------------------------------

@Entity
@Table(name = "ubicacion")
public class Ubicacion implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private long id_ubicacion;

    // Código libre para identificar la ubicación (ej. A-12-3)
    @Column(name = "codigo", length = 50, unique = false)
    private String codigo;

    // Pasillo o zona del almacén
    @Column(name = "pasillo", length = 50)
    private String pasillo;

    // Estantería/mesa/sector
    @Column(name = "estanteria", length = 50)
    private String estanteria;

    // Nivel o altura en la estantería
    @Column(name = "nivel")
    private Integer nivel;

    @Column(name = "mesa")
    private char mesa;

    @Column(name = "exterior")
    private boolean exterior;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    public long getIdUbicacion() {
        return id_ubicacion;
    }

    public void setIdUbicacion(long idUbicacion) {
        this.id_ubicacion = idUbicacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPasillo() {
        return pasillo;
    }

    public void setPasillo(String pasillo) {
        this.pasillo = pasillo;
    }

    public String getEstanteria() {
        return estanteria;
    }

    public void setEstanteria(String estanteria) {
        this.estanteria = estanteria;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public char getMesa() {
        return mesa;
    }

    public void setMesa(char mesa) {
        this.mesa = mesa;
    }

    public boolean isExterior() {
        return exterior;
    }

    public void setExterior(boolean exterior) {
        this.exterior = exterior;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Ubicacion [id_ubicacion=" + id_ubicacion + ", codigo=" + codigo + ", pasillo=" + pasillo
                + ", estanteria=" + estanteria + ", nivel=" + nivel + ", mesa=" + mesa
                + ", exterior=" + exterior + ", descripcion=" + descripcion + "]";
    }
}
