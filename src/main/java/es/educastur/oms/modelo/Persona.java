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
//Fecha: 2025-11-10
//Descripción: Clase VO de personas.
//--------------------------------------------------------

@Entity
@Table(name = "personas")
public class Persona implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_persona;
	
	@Column(name = "nombre", length = 50)
	private String nombre;
	
	@Column(name = "email", length = 50)
    private String email;

	public Persona() {
		super();
	}

	public Persona(Long id_persona, String nombre, String email) {
		super();
		this.id_persona = id_persona;
		this.nombre = nombre;
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id_persona;
	}

	public void setId(Long id) {
		this.id_persona = id;
	}

	@Override
	public String toString() {
		return "Persona [id_persona=" + id_persona + ", nombre=" + nombre + ", email=" + email + "]";
	}
}