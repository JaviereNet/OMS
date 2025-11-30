package es.educastur.oms.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import es.educastur.oms.servicios.ServicioCliente;
import es.educastur.oms.servicios.ServicioStock;
import es.educastur.oms.servicios.ServicioProducto;


/**
 * Controlador para la gestión del almacén.
 * Maneja las rutas relacionadas con la visualización de la información del almacén
 * para diferentes tipos de usuarios: administrador, personal y clientes.
 */

@Controller
@SessionAttributes({"nombreUsuario", "id_Persona", "id_Cliente", "UsuarioCliente", "UsuarioPersona"})
public class AlmacenController {

    @Autowired
    @Qualifier("servicioProductoImpl")
    private ServicioProducto S_producto;

    @Autowired
    @Qualifier("servicioStockImpl")
    private ServicioStock S_stock;


 	@Autowired
 	@Qualifier("servicioClienteImpl")
 	private ServicioCliente S_cliente;


    /**
     * Maneja la página de inicio del almacén.
     *
     * @param model Modelo de Spring para pasar atributos a la vista.
     * @return La vista "inicio".
     */
	@GetMapping("/inicio")
    public String inicioAlmacen(Model model) {
        model.addAttribute("mensaje", "Página inicial del almacén");
        model.addAttribute("productos", S_producto.listarProductos());
        return "inicio";
    }
    
    // Redirigir la raíz del sitio a /inicio para que la app arranque en la página pública de inicio
    @GetMapping("/")
    public String raiz() {
        return "redirect:/inicio";
    }

    /**
     * Maneja la página de inicio para administradores.
     * 
     * @param model Modelo de Spring para pasar atributos a la vista.
     * @return La vista "inicio-admin".
     */
	@GetMapping("/inicio-admin")
    public String inicioAlmacenAdmin(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
        model.addAttribute("mensaje", "Página inicial del almacén(Usuario administrador)");
        model.addAttribute("UsuarioActual", nombreUsuario);
        // Asegurar que el fragmento header reconoce al usuario como 'UsuarioPersona'
        model.addAttribute("UsuarioPersona", nombreUsuario);
        model.addAttribute("totalPlantas", S_producto.contadorProductos());
        // calcular stock total sumando las cantidades por producto
        long totalEjemplares = S_producto.listarProductos().stream()
                .mapToLong(p -> S_stock.obtenerStockTotalPorProducto(p))
                .sum();
        model.addAttribute("totalEjemplares", totalEjemplares);
        model.addAttribute("productos", S_producto.listarProductos());
        return "inicio-admin";
    }
    
    /**
     * Maneja la página de inicio para personal del almacén.
     *
     * @param model Modelo de Spring para pasar atributos a la vista.
     * @return La vista "inicio-personal".
     */
	@GetMapping("/inicio-personal")
    public String inicioAlmacenPersonal(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
        model.addAttribute("mensaje", "Página inicial del almacén(Usuario personal)");
        model.addAttribute("UsuarioActual", nombreUsuario);
        // Asegurar que el fragmento header reconoce al usuario como 'UsuarioPersona'
        model.addAttribute("UsuarioPersona", nombreUsuario);
        model.addAttribute("totalPlantas", S_producto.contadorProductos());
        // calcular stock total sumando las cantidades por producto
        long totalEjemplares = S_producto.listarProductos().stream()
                .mapToLong(p -> S_stock.obtenerStockTotalPorProducto(p))
                .sum();
        model.addAttribute("totalEjemplares", totalEjemplares);
        model.addAttribute("productos", S_producto.listarProductos());
        return "inicio-personal";
    }

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }
}
