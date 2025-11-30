package es.educastur.oms.modelo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2025-11-15
//Descripción: Clase VO de credenciales.
//--------------------------------------------------------


@Entity
@Table(name = "credenciales")
public class Credenciales implements Serializable {
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "usuario", length = 50)
	private String usuario;
	
	@Column(name = "password", length = 100)
	private String password;
	
	@OneToOne
	@JoinColumn(name = "id_persona")
	private Persona persona;
	
	@OneToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;


	public Credenciales() {
		super();
	}	
	
	public Credenciales(String usuario, String password) {
		super();
		this.usuario = usuario;
		this.password = password;
	}

	public Credenciales(long id, String usuario, String password, Persona persona) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.password = password;
		this.persona = persona;
	}

	public Credenciales(long id, String usuario, String password, Persona persona, Cliente cliente) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.password = password;
		this.persona = persona;
		this.cliente = cliente;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Credenciales [id=" + id + ", usuario=" + usuario + ", password=" + password + ", persona=" + persona
				+ ", cliente=" + cliente + "]";
	}
}