package es.educastur.oms.controlador;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import es.educastur.oms.modelo.Credenciales;
import es.educastur.oms.modelo.Persona;
import es.educastur.oms.servicios.ServicioCredenciales;
import es.educastur.oms.servicios.ServicioPersona;

@Controller
@SessionAttributes({"nombreUsuario", "id_Persona", "id_Cliente", "UsuarioCliente", "UsuarioPersona"})
public class PersonalController {
	
	@Autowired
	@Qualifier("servicioPersonaImpl")
    private ServicioPersona S_persona;
	
	@Autowired
	@Qualifier("servicioCredencialesImpl")
    private ServicioCredenciales S_credenciales;
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$");

	/**
     * Muestra el formulario para añadir personal.
     * 
     * @param nombreUsuario Nombre del usuario en sesión.
     * @param model Modelo de datos para la vista.
     * @return Nombre de la vista "añadirpersonal".
     */
	@GetMapping("/anadirpersonal")
	public String anadirPersonal(@ModelAttribute("nombreUsuario") String nombreUsuario,
	                             @RequestParam(value = "success", required = false) String success,
	                             Model model) {
	    model.addAttribute("mensaje", "Añadir personal al almacén");
	    model.addAttribute("UsuarioActual", nombreUsuario);
	    model.addAttribute("persona", new Persona());
	    model.addAttribute("credenciales", new Credenciales());

	    if ("true".equals(success)) {
	        model.addAttribute("success", "✅ Personal registrado exitosamente.");
	    }

	    return "añadirpersonal";
	}

	@PostMapping("/anadirpersonal")
	public String registrarPersonal(@ModelAttribute("persona") Persona persona,
	                                @RequestParam("username") String usuario,
	                                @RequestParam("password") String password,
	                                Model model) {
	    boolean hayErrores = false;

	    // Validaciones
	    if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
	        model.addAttribute("nombreError", "El nombre no puede estar vacío.");
	        hayErrores = true;
	    } else if (!persona.getNombre().matches("[a-zA-Z ]+")) {
	        model.addAttribute("nombreError", "El nombre solo debe contener letras y espacios.");
	        hayErrores = true;
	    }

	    if (persona.getEmail() == null || persona.getEmail().trim().isEmpty()) {
	        model.addAttribute("emailError", "El correo electrónico no puede estar vacío.");
	        hayErrores = true;
	    } else if (!EMAIL_PATTERN.matcher(persona.getEmail()).matches()) {
	        model.addAttribute("emailError", "El correo electrónico tiene un formato incorrecto.");
	        hayErrores = true;
	    } else if (S_persona.existPersonaPorEmail(persona.getEmail())) {
	        model.addAttribute("emailError", "El correo electrónico ya está registrado.");
	        hayErrores = true;
	    }

	    if (usuario == null || usuario.trim().isEmpty()) {
	        model.addAttribute("usuarioError", "El nombre de usuario no puede estar vacío.");
	        hayErrores = true;
	    } else if (S_credenciales.existeNombreUsuario(usuario)) {
	        model.addAttribute("usuarioError", "El nombre de usuario ya está registrado.");
	        hayErrores = true;
	    } else if (usuario.length() < 4) {
	        model.addAttribute("usuarioError", "El usuario debe tener al menos 4 caracteres.");
	        hayErrores = true;
	    } else if (!usuario.matches("[a-zA-Z0-9]+")) {
	        model.addAttribute("usuarioError", "El usuario solo debe contener letras y números.");
	        hayErrores = true;
	    } else if (!usuario.matches(".*[a-zA-Z].*") || !usuario.matches(".*[0-9].*")) {
	        model.addAttribute("usuarioError", "El usuario debe contener al menos una letra y un número.");
	        hayErrores = true;
	    }

	    if (password == null || password.trim().isEmpty()) {
	        model.addAttribute("passwordError", "La contraseña no puede estar vacía.");
	        hayErrores = true;
	    } else if (password.contains(" ")) {
	        model.addAttribute("passwordError", "La contraseña no puede contener espacios.");
	        hayErrores = true;
	    } else if (password.length() < 8) {
	        model.addAttribute("passwordError", "La contraseña debe tener al menos 8 caracteres.");
	        hayErrores = true;
	    } else if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
	        model.addAttribute("passwordError", "La contraseña debe contener al menos un carácter especial.");
	        hayErrores = true;
	    }

	    if (hayErrores) {
	        return "añadirpersonal";
	    }

	    Persona personaGuardada = S_persona.guardarPersona(persona);
	    if (personaGuardada == null || personaGuardada.getId() <= 0) {
	        model.addAttribute("error", "Error al registrar la persona.");
	        return "añadirpersonal";
	    }

	    Credenciales credenciales = new Credenciales();
	    credenciales.setUsuario(usuario);
	    credenciales.setPassword(password);
	    credenciales.setPersona(personaGuardada);

	    Credenciales credencialesGuardadas = S_credenciales.guardarCredenciales(credenciales);
	    if (credencialesGuardadas == null || credencialesGuardadas.getId() <= 0) {
	        model.addAttribute("error", "Error al registrar las credenciales.");
	        return "añadirpersonal";
	    }

	    return "redirect:/anadirpersonal?success=true";
	}
}
