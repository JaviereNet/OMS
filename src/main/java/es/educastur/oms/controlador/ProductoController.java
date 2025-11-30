package es.educastur.oms.controlador;

import java.text.Normalizer;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import es.educastur.oms.modelo.Producto;
import es.educastur.oms.servicios.ServicioProducto;
import es.educastur.oms.servicios.ServicioStock;


/**
 * Controlador para la gestión de productos en el almacén.
 * Proporciona funcionalidades para agregar, modificar y eliminar productos.
 */
@Controller
@SessionAttributes({"nombreUsuario", "id_Persona", "id_Cliente", "UsuarioCliente", "UsuarioPersona"})
public class ProductoController {

    @Autowired
    @Qualifier("servicioProductoImpl")
    private ServicioProducto S_producto;

    @Autowired
    @Qualifier("servicioStockImpl")
    private ServicioStock S_stock;

   

    /**
     * Muestra la página de administración de productos.
     * @param nombreUsuario Nombre del usuario en sesión.
     * @param model Modelo de la vista.
     * @return Vista de administración de productos.
     */
    @GetMapping("/productos-admin")
    public String productosAdmin(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
        model.addAttribute("mensaje", "Gestión de productos (Usuario administrador)");
        model.addAttribute("UsuarioActual", nombreUsuario);
        return "productos-admin";
    }

    /**
     * Muestra el formulario para añadir un nuevo producto.
     * @param nombreUsuario Nombre del usuario en sesión.
     * @param model Modelo de la vista.
     * @return Vista para añadir productos.
     */
	@GetMapping("/productos-adminAnadir")
	public String productosAdminAnadir(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
		try {
			model.addAttribute("UsuarioActual", nombreUsuario);
			model.addAttribute("producto", new Producto());
		} catch (Exception e) {
			model.addAttribute("error", "Error al obtener los datos del producto: " + e.getMessage());
			return "redirect:/productos-admin";
		}
		return "productos-adminAñadir";
	}

    /**
     * Guarda un nuevo producto en la base de datos.
     * @param producto Producto a guardar.
     * @param resultado Resultado de validación.
     * @return Redirección a la vista de administración de productos.
     */
    @PostMapping("/productos-adminAnadir")
    public String guardarProducto(@ModelAttribute Producto producto, BindingResult resultado, Model model) {
        String codigo = producto.getCodigo();
        String nombre = producto.getNombre();
        String descripcion = producto.getDescripcion();

        // Validaciones
        // Allow alphanumeric codes (letters + digits), no spaces or special chars
        if (codigo == null || !codigo.matches("^[A-Za-z0-9]+$")) {
            resultado.rejectValue("codigo", "codigo.invalid", "El código solo puede contener letras y dígitos, sin espacios ni caracteres especiales.");
        }

        if (codigo != null) {
            String codigoNormalizado = Normalizer.normalize(codigo, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            producto.setCodigo(codigoNormalizado.toUpperCase());
        }

        if (S_producto.existProductoPorCodigo(producto.getCodigo())) {
            resultado.rejectValue("codigo", "codigo.exists", "Ya hay un producto con ese código.");
        }

        if (nombre == null || !nombre.matches("^[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+$")) {
            resultado.rejectValue("nombre", "nombre.invalid", "El nombre solo puede contener letras y espacios.");
        }

        if (descripcion == null || !descripcion.matches("^[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+$")) {
            resultado.rejectValue("descripcion", "descripcion.invalid", "La descripción solo puede contener letras y espacios.");
        }

        if (resultado.hasErrors()) {
            return "productos-adminAñadir";
        }

        try {
            if (codigo != null) producto.setCodigo(codigo.toUpperCase());
            if (nombre != null) producto.setNombre(nombre.toUpperCase());
            if (descripcion != null) producto.setDescripcion(descripcion.toUpperCase());
            producto.setPrecio(10);
            S_producto.guardarProducto(producto);
            model.addAttribute("mensaje", "✅ Producto añadido con éxito.");
        } catch (Exception e) {
            model.addAttribute("error", "❌ Error al añadir el producto: " + e.getMessage());
        }

        return "productos-adminAñadir";
    }
    
    @GetMapping("/productos-adminModificar")
    public String productosAdminModificar(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
        try {
            model.addAttribute("UsuarioActual", nombreUsuario);
            model.addAttribute("productos", S_producto.listarProductos());
            model.addAttribute("producto", new Producto());
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener los datos del producto: " + e.getMessage());
            return "redirect:/productos-admin";
        }
        return "productos-adminModificar";
    }

	@PostMapping("/productos-adminModificar")
	public String modificarProducto(@RequestParam(value = "producto", required = false) String codigoProductoParam,
					  @ModelAttribute Producto producto,
				              BindingResult resultado, Model model,
				              @ModelAttribute("nombreUsuario") String nombreUsuario) {

		// Si el select de la vista tiene nombre 'producto' nos lo pasarán aquí; si no, intentamos leer del objeto producto
		String codigoProducto = (codigoProductoParam != null && !codigoProductoParam.isBlank()) ? codigoProductoParam : producto.getCodigo();

		// Aseguramos que la lista de productos esté siempre en el modelo (para repoblar el select)
		model.addAttribute("productos", S_producto.listarProductos());
		// Mantenemos el usuario actual para el header
		model.addAttribute("UsuarioActual", nombreUsuario);
		// También mantenemos el objeto producto enviado (útil cuando hay errores de validación)
		model.addAttribute("producto", producto);

		if (codigoProducto == null || codigoProducto.isBlank()) {
			resultado.rejectValue("codigo", "codigo.invalid", "No se ha seleccionado ningún producto para modificar.");
			return "productos-adminModificar";
		}

		Optional<Producto> productoExistente = S_producto.buscarProductoPorId(codigoProducto);

		// Validaciones
		if (productoExistente.isEmpty()) {
			resultado.rejectValue("codigo", "codigo.invalid", "Producto con el código " + codigoProducto + " no encontrado.");
			return "productos-adminModificar";
		}

		String nombre = producto.getNombre();
		String descripcion = producto.getDescripcion();

		if (nombre == null || !nombre.matches("^[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+$")) {
			resultado.rejectValue("nombre", "nombre.invalid", "El nombre solo puede contener letras y espacios.");
		}

		if (descripcion == null || !descripcion.matches("^[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+$")) {
			resultado.rejectValue("descripcion", "descripcion.invalid", "La descripción solo puede contener letras y espacios.");
		}

		if (resultado.hasErrors()) {
			// Dejamos el producto enviado en el modelo para que se muestren los valores y errores
			model.addAttribute("producto", producto);
			return "productos-adminModificar";
		}
		
		try {

			Producto productoModificada = productoExistente.get();

			// Actualizamos todos los campos editables si vienen en el formulario
			if (producto.getCodigo() != null && !producto.getCodigo().isBlank()) {
				String codigoNormalizado = Normalizer.normalize(producto.getCodigo(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
				productoModificada.setCodigo(codigoNormalizado.toUpperCase());
			}
			if (nombre != null) productoModificada.setNombre(nombre.toUpperCase());
			if (descripcion != null) productoModificada.setDescripcion(descripcion.toUpperCase());
			if (producto.getCategoria() != null) productoModificada.setCategoria(producto.getCategoria().toUpperCase());
			if (producto.getMarca() != null) productoModificada.setMarca(producto.getMarca().toUpperCase());
			if (producto.getPrecio() != 0) productoModificada.setPrecio(producto.getPrecio());

			S_producto.modificarProducto(productoModificada);
			// enviamos el producto modificado al modelo para que el formulario muestre los valores actualizados
			model.addAttribute("producto", productoModificada);
			model.addAttribute("mensaje", "✅ Producto modificado con éxito.");

		} catch (Exception e) {
			model.addAttribute("error", "❌ Error al modificar el producto: " + e.getMessage());
		}
		
		return "productos-adminModificar";
	}


    @GetMapping("/productos-adminEliminar")
    public String productosAdminEliminar(@ModelAttribute("nombreUsuario") String nombreUsuario, 
                                       Model model) {
        try {
            model.addAttribute("mensaje", "Borrar producto (Usuario administrador)");
            model.addAttribute("UsuarioActual", nombreUsuario);
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener los datos del producto: " + e.getMessage());
            System.err.println("Error al obtener los datos del producto: " + e.getMessage());
            return "redirect:/productos-admin";
        }
        return "productos-adminEliminar";
    }

    /**
     * Elimina un producto de la base de datos.
     * @param codigo Código del producto a eliminar.
     * @param model Modelo de la vista.
     * @return Redirección a la vista de administración de productos.
     */
    @PostMapping("/productos-adminEliminar")
    public String eliminarProducto(@RequestParam("codigo") String codigo, Model model) {
        try {
            Optional<Producto> producto_codigo = S_producto.buscarProductoPorId(codigo);

            if (producto_codigo.isEmpty()) {
                model.addAttribute("error", "Producto con el código " + codigo + " no encontrado.");
                System.err.println("Producto no encontrado.");
                return "productos-adminEliminar";
            }
            // Comprobar si existe stock asociado al producto
            Producto productoObj = producto_codigo.get();
            int totalStock = S_stock.obtenerStockTotalPorProducto(productoObj);
            if (totalStock > 0) {
                model.addAttribute("error", "El producto tiene stock asociado y no puede ser eliminado.");
                return "productos-adminEliminar";
            }
            S_producto.eliminarProducto(codigo);
            model.addAttribute("mensaje", "Producto eliminado con éxito.");
            System.out.println("Producto eliminado.");
            return "productos-admin";
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el producto: " + e.getMessage());
            System.err.println("Error al eliminar el producto: " + e.getMessage());
            return "redirect:/productos-adminEliminar";
        }
    }
}

