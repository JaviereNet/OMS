package es.educastur.oms.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2025-01-12
//Descripción: Clase VO de clientes.
//--------------------------------------------------------

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_cliente;

	@Column(name = "nombre", length = 50)
	private String nombre;

	@Column(name = "nif_nie", length = 10, unique = true)
	private String nif_nie;
	
	@Column(name = "fechaNacimiento")
	private LocalDate fechaNacimiento;
	
	@Column(name = "fechaRegistro")
	private LocalDate fechaRegistro;
	
	@Column(name = "direccionEnvio", length = 255, nullable = false)
    private String direccionEnvio;
    
    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;
	
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
        name = "productos_clientes",
        joinColumns = @JoinColumn(name = "id_cliente"),
        inverseJoinColumns = @JoinColumn(name = "id_producto")
    )
	private List<Producto> productosFavoritos = new ArrayList<>();

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Pedido> pedidos;


	public Cliente() {
		super();
	}

	public Cliente(Long id_cliente, String nombre, String nif_nie, LocalDate fechaNacimiento, LocalDate fechaRegistro,
				String direccionEnvio, String telefono, String email, List<Producto> productosFavoritos) {
		super();
		this.id_cliente = id_cliente;
		this.nombre = nombre;
		this.nif_nie = nif_nie;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaRegistro = fechaRegistro;
		this.direccionEnvio = direccionEnvio;
		this.telefono = telefono;
		this.email = email;
		this.productosFavoritos = (productosFavoritos != null) ? new ArrayList<>(productosFavoritos) : new ArrayList<>();
	}

	public Cliente(Long id_cliente, String nombre, String nif_nie, LocalDate fechaNacimiento, LocalDate fechaRegistro,
				String direccionEnvio, String telefono, String email, List<Producto> productosFavoritos, List<Pedido> pedidos) {
		super();
		this.id_cliente = id_cliente;
		this.nombre = nombre;
		this.nif_nie = nif_nie;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaRegistro = fechaRegistro;
		this.direccionEnvio = direccionEnvio;
		this.telefono = telefono;
		this.email = email;
		this.productosFavoritos = (productosFavoritos != null) ? new ArrayList<>(productosFavoritos) : new ArrayList<>();
		this.pedidos = (pedidos != null) ? new ArrayList<>(pedidos) : new ArrayList<>();
	}

	public Long getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(Long id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNif_nie() {
		return nif_nie;
	}

	public void setNif_nie(String nif_nie) {
		this.nif_nie = nif_nie;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getDireccionEnvio() {
		return direccionEnvio;
	}

	public void setDireccionEnvio(String direccionEnvio) {
		this.direccionEnvio = direccionEnvio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Producto> getProductosFavoritos() {
		return productosFavoritos;
	}

	public void setProductosFavoritos(List<Producto> productosFavoritos) {
		this.productosFavoritos = (productosFavoritos != null) ? new ArrayList<>(productosFavoritos) : new ArrayList<>();
	}

    public void addProducto(Producto p) {
        if (p == null) return;
        if (!this.productosFavoritos.contains(p)) {
            this.productosFavoritos.add(p);
            if (p.getClientesFavoritos() == null) p.setClientesFavoritos(new java.util.ArrayList<>());
            if (!p.getClientesFavoritos().contains(this)) p.getClientesFavoritos().add(this);
        }
    }

    public void removeProducto(Producto p) {
        if (p == null) return;
        if (this.productosFavoritos.remove(p)) {
            if (p.getClientesFavoritos() != null) p.getClientesFavoritos().remove(this);
        }
    }

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	@Override
	public String toString() {
		return "Cliente [id_cliente=" + id_cliente + ", nombre=" + nombre + ", nif_nie=" + nif_nie
				+ ", fechaNacimiento=" + fechaNacimiento + ", fechaRegistro=" + fechaRegistro + "]";
	}
}
