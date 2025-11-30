package es.educastur.oms.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import es.educastur.oms.servicios.ServicioProducto;

/**
 * Controlador para gestionar los mensajes y anotaciones en el sistema (deshabilitado).
 */
@Controller
@SessionAttributes({"nombreUsuario", "id_Persona", "id_Cliente", "UsuarioCliente", "UsuarioPersona"})
public class MensajeController {

    @Autowired
    @Qualifier("servicioProductoImpl")
    private ServicioProducto S_producto;

    /**
     * Muestra la vista de administración de mensajes con filtros opcionales.
     * 
     * @param nombre Nombre de la persona asociada al mensaje.
     * @param fechaInicio Fecha de inicio del filtro.
     * @param fechaFin Fecha de fin del filtro.
     * @param producto Tipo de producto asociado.
     * @param nombreUsuario Nombre del usuario en sesión.
     * @param model Modelo de datos para la vista.
     * @return Nombre de la vista "gestion-mensajes".
     */
    @GetMapping("/gestion-mensajes")
    public String MensajesAdminRedirect(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
        model.addAttribute("error", "La funcionalidad de mensajes ha sido deshabilitada.");
        return "redirect:/gestion-stock";
    }
    
    /**
     * Registra una anotación en un ejemplar.
     * 
     * @param ejemplarId ID del ejemplar asociado a la anotación.
     * @param mensajeTexto Contenido del mensaje.
     * @param nombreUsuario Nombre del usuario en sesión.
     * @param id_Persona ID de la persona asociada.
     * @param model Modelo de datos para la vista.
     * @return Redirección a la vista "gestion-mensajes".
     */
    @PostMapping("/gestion-mensajes")
    public String realizarAnotacionDisabled() {
        // funcionalidad deshabilitada
        return "redirect:/gestion-stock";
    }
}
