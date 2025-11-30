package es.educastur.oms.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2025-01-12
//Descripción: Clase VO de historiales.
//--------------------------------------------------------

@Entity
@Table(name = "historial")
public class Historial implements Serializable{

    private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_historial;
	
	@Column(name = "nh", length = 50, unique = true)
	private String nh;
	
	@Column(name = "actualizado")
    private LocalDate actualizado;
	
	// Historial asociado a un pedido (registro de cambios de estado)
	@OneToOne
	@JoinColumn(name = "id_pedido", unique = true)
	private Pedido pedido;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="id_historial")
	private List<HistorialEntrada> entradas = new LinkedList<HistorialEntrada>();

	public Historial() {
		super();
	}

	public Historial(Long id_historial, String nh, LocalDate actualizado) {
		super();
		this.id_historial = id_historial;
		this.nh = nh;
		this.actualizado = actualizado;
	}

	public Historial(Long id_historial, String nh, LocalDate actualizado, Pedido pedido) {
		super();
		this.id_historial = id_historial;
		this.nh = nh;
		this.actualizado = actualizado;
		this.pedido = pedido;
	}

	public Long getId_historial() {
		return id_historial;
	}

	public void setId_historial(Long id_historial) {
		this.id_historial = id_historial;
	}

	public String getNh() {
		return nh;
	}

	public void setNh(String nh) {
		this.nh = nh;
	}

	public LocalDate getActualizado() {
		return actualizado;
	}

	public void setActualizado(LocalDate actualizado) {
		this.actualizado = actualizado;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<HistorialEntrada> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<HistorialEntrada> entradas) {
		this.entradas = entradas;
	}

	@Override
	public String toString() {
		return "Historial [id_historial=" + id_historial + ", nh=" + nh + ", actualizado=" + actualizado + ", pedido="
				+ pedido + "]";
	}
}
