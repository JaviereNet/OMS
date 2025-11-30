package es.educastur.oms.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.educastur.oms.modelo.Persona;
import es.educastur.oms.repositorios.PersonaRepository;
import es.educastur.oms.servicios.ServicioPersona;

@Service
public class ServicioPersonaImpl implements ServicioPersona {
	@Autowired
	private PersonaRepository persona_R;

	@Override
	public List<Persona> listarPersonas() {
		return persona_R.findAll();
	}

	@Override
	public Optional<Persona> buscarPorId(Long id) {
		return persona_R.findById(id);
	}

	@Override
	public Persona guardarPersona(Persona persona) {
	    return persona_R.save(persona);
	}

	@Override
	public void eliminarPersona(Long id) {
		persona_R.deleteById(id);
	}
	
	public boolean existPersonaPorEmail(String email) {
        return persona_R.existePersonaPorEmail(email);
    }

	public Optional<Persona> buscarPorNombre(String nombre) {
        return persona_R.findByNombre(nombre);
    }
}