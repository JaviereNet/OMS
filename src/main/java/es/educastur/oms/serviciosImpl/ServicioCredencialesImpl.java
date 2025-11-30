package es.educastur.oms.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import es.educastur.oms.modelo.Credenciales;
import es.educastur.oms.modelo.Persona;
import es.educastur.oms.repositorios.CredencialesRepository;
import es.educastur.oms.repositorios.PersonaRepository;
import es.educastur.oms.servicios.ServicioCredenciales;

import jakarta.annotation.PostConstruct;

@Service
public class ServicioCredencialesImpl implements ServicioCredenciales {

	@Autowired
    private CredencialesRepository credenciales_R;

    @Autowired
    private PersonaRepository personaRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Credenciales> listarCredenciales() {
        return credenciales_R.findAll();
    }

    @Override
    public Optional<Credenciales> buscarPorId(long id) {
        return credenciales_R.findById(id);
    }

    @Override
    public Optional<Credenciales> buscarPorUsuario(String usuario) {
        return credenciales_R.findByUsuario(usuario);
    }

    @Override
    public boolean existeNombreUsuario(String usuario) {
        return credenciales_R.existeNombreUsuario(usuario);
    }
    
    @PostConstruct
    public void crearAdminSiNoExiste() {
        // Verificar si ya existe el administrador
        Optional<Credenciales> admin = credenciales_R.findByUsuario("admin");
        if (admin.isEmpty()) {
            // Crear un nuevo usuario administrador
            Persona personaAdmin = new Persona();
            personaAdmin.setNombre("Admin");
            personaAdmin.setEmail("admin@admin.com");
            personaAdmin = personaRepository.save(personaAdmin);

            Credenciales credencialesAdmin = new Credenciales();
            credencialesAdmin.setUsuario("admin");
            credencialesAdmin.setPassword(passwordEncoder.encode("admin"));
            credencialesAdmin.setPersona(personaAdmin);

            credenciales_R.save(credencialesAdmin);
            System.out.println("Administrador creado con éxito.");
        } else {
            System.out.println("El usuario administrador ya existe.");
        }
    }

    @Override
    public Credenciales guardarCredenciales(Credenciales credenciales) {
       if (credenciales.getPassword() == null || credenciales.getPassword().isEmpty()) {
    	        System.err.println("La contraseña no puede ser nula o vacía");
    	}

    	credenciales.setPassword(passwordEncoder.encode(credenciales.getPassword()));
        return credenciales_R.save(credenciales);
    }

    @Override
    public void eliminarCredenciales(long id) {
        credenciales_R.deleteById(id);
    }
    
    @Override
    public boolean autenticar(Credenciales credenciales) {
        Optional<Credenciales> credencialesDB = buscarPorUsuario(credenciales.getUsuario());
        if (credencialesDB.isEmpty()) {
            return false;
        }
        String encodedPassword = credencialesDB.get().getPassword();
        String rawPassword = credenciales.getPassword();
        if (encodedPassword == null || rawPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
