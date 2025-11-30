package es.educastur.oms.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

//--------------------------------------------------------
//Autor: Javier García Ramos (adaptado)
//Fecha: 2025-11-17
//Descripción: Clase VO de productos (generalización de Planta).
//--------------------------------------------------------

@Entity
@Table(name = "productos")
public class Producto implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true)
    private String codigo;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "precio")
    private double precio;

    @ManyToMany(mappedBy = "productosFavoritos", fetch = FetchType.LAZY)
    private List<Cliente> clientesFavoritos = new ArrayList<>();

    public Producto() {
        super();
    }

    public Producto(String codigo, String nombre, String descripcion) {
        super();
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Producto(Long id, String codigo, String nombre, String descripcion, double precio, String categoria, String marca, List<Cliente> clientesFavoritos) {
        super();
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.marca = marca;
        this.clientesFavoritos = (clientesFavoritos != null) ? new ArrayList<>(clientesFavoritos) : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public List<Cliente> getClientesFavoritos() {
        return clientesFavoritos;
    }

    public void setClientesFavoritos(List<Cliente> clientesFavoritos) {
        this.clientesFavoritos = (clientesFavoritos != null) ? new ArrayList<>(clientesFavoritos) : new ArrayList<>();
    }

    public void addClienteFavorito(Cliente c) {
        if (c == null) return;
        if (!this.clientesFavoritos.contains(c)) {
            this.clientesFavoritos.add(c);
            if (c.getProductosFavoritos() == null) c.setProductosFavoritos(new java.util.ArrayList<>());
            if (!c.getProductosFavoritos().contains(this)) c.getProductosFavoritos().add(this);
        }
    }

    public void removeClienteFavorito(Cliente c) {
        if (c == null) return;
        if (this.clientesFavoritos.remove(c)) {
            if (c.getProductosFavoritos() != null) c.getProductosFavoritos().remove(this);
        }
    }

    @Override
    public String toString() {
        return "Producto{" +
               "id=" + id +
               ", codigo='" + codigo + '\'' +
               ", nombre='" + nombre + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", categoria='" + categoria + '\'' +
               ", marca='" + marca + '\'' +
               ", precio=" + precio +
               '}';
    }
}
