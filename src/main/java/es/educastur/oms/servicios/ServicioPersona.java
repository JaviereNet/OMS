package es.educastur.oms.servicios;

import java.util.List;
import java.util.Optional;
import es.educastur.oms.modelo.Persona;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2025-02-13
//Descripción: Interfaz de servicio para gestionar las operaciones
//relacionadas con las personas. Proporciona métodos para listar 
//todas las personas, buscar por ID, guardar nuevas personas y 
//eliminar personas existentes.
//--------------------------------------------------------


public interface ServicioPersona {
	
	List<Persona> listarPersonas();
    Optional<Persona> buscarPorId(Long id);
    Persona guardarPersona(Persona persona);
    void eliminarPersona(Long id);
    boolean existPersonaPorEmail(String email);
    Optional<Persona> buscarPorNombre(String nombre);
	
}