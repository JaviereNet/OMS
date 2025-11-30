package es.educastur.oms.servicios;

import java.util.List;
import java.util.Optional;
import es.educastur.oms.modelo.Credenciales;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2025-02-13
//Descripción: Interfaz de servicio para gestionar las operaciones
//relacionadas con las credenciales de usuario. Proporciono métodos 
//para listar, buscar, guardar, eliminar y autenticar credenciales.
//--------------------------------------------------------

public interface ServicioCredenciales {
	
	List<Credenciales> listarCredenciales();
    Optional<Credenciales> buscarPorId(long id);
    Optional<Credenciales> buscarPorUsuario(String usuario);
    boolean existeNombreUsuario(String usuario);
    Credenciales guardarCredenciales(Credenciales credenciales);
    void eliminarCredenciales(long id);
    boolean autenticar(Credenciales credenciales);
}
