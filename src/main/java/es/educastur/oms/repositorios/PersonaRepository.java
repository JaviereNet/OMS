package es.educastur.oms.repositorios;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import es.educastur.oms.modelo.Persona;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2024-12-10
//Descripción: Repositorio de Personas.
//--------------------------------------------------------

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
	
	@Query("SELECT COUNT(p) > 0 FROM Persona p WHERE p.email = :email")
    boolean existePersonaPorEmail(@Param("email") String email);	
	
	boolean existsByNombre(String nombre);

	@Query("SELECT p FROM Persona p WHERE p.nombre = :nombre")
    Optional<Persona> findByNombre(@Param("nombre") String nombre);
}
