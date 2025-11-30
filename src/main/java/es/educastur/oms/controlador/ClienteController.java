package es.educastur.oms.controlador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import es.educastur.oms.modelo.Cliente;
import es.educastur.oms.modelo.EstadoPedido;
import es.educastur.oms.modelo.Pedido;
import es.educastur.oms.modelo.Producto;
import es.educastur.oms.modelo.PedidoItem;
import es.educastur.oms.servicios.ServicioCliente;
import es.educastur.oms.servicios.ServicioPedido;
import es.educastur.oms.servicios.ServicioProducto;
import es.educastur.oms.servicios.ServicioStock;

import jakarta.servlet.http.HttpSession;
import java.text.DecimalFormat;

@Controller
@SessionAttributes({"nombreUsuario", "id_Persona", "id_Cliente", "UsuarioCliente", "UsuarioPersona"})
public class ClienteController { 
	
	 @Autowired
	 @Qualifier("servicioProductoImpl")
	 private ServicioProducto S_producto;

     @Autowired
     @Qualifier("servicioClienteImpl")
     private ServicioCliente S_cliente;
     
     @Autowired
     @Qualifier("servicioPedidoImpl")
     private ServicioPedido S_pedido;

    @Autowired
    @Qualifier("servicioStockImpl")
    private ServicioStock S_stock;

	LocalDate Fechahoy = LocalDate.now();
	String fechahoyFormateada = Fechahoy.getDayOfMonth() + " de " +
			 Fechahoy.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " de " +
			 Fechahoy.getYear();
	
    /**
     * Maneja la página de inicio para clientes.
     * 
     * @param nombreUsuario Nombre del usuario cliente.
     * @param model Modelo de Spring para pasar atributos a la vista.
     * @return La vista "inicio-cliente".
     */
	
	@GetMapping("/inicio-cliente")
    public String inicioAlmacenCliente(@ModelAttribute("nombreUsuario") String nombreUsuario,
                                      @ModelAttribute("id_Cliente") Long id_Cliente,
                                      @ModelAttribute("UsuarioCliente") Cliente Usuario,
                                      Model model) {
        try {
            Optional<Cliente> clienteActual = S_cliente.buscarPorId(id_Cliente);

            if (clienteActual.isPresent()) {
                Cliente cliente = clienteActual.get();
                List<Producto> productosFavoritos = cliente.getProductosFavoritos();

                model.addAttribute("UsuarioActual", nombreUsuario);
                model.addAttribute("emailCliente", Usuario.getEmail());
                model.addAttribute("productosFavoritos", productosFavoritos);

                // Exponer un conjunto de códigos de producto favoritos para que la vista
                // pueda comprobar membership de forma segura (evita depender de equals())
                Set<String> favoritosCodigos = productosFavoritos.stream()
                        .map(Producto::getCodigo)
                        .collect(Collectors.toSet());
                model.addAttribute("favoritosCodigos", favoritosCodigos);

            } else {
                model.addAttribute("productosFavoritos", Collections.emptyList());
                model.addAttribute("favoritosCodigos", Collections.emptySet());
            }

            model.addAttribute("mensaje", "Página inicial del almacén (Usuario cliente)");
            List<Producto> productos = S_producto.listarProductos();
            model.addAttribute("productos", productos);

            // --- nuevo: preparar mapa con precios formateados para evitar instanciar clases desde Thymeleaf ---
            Map<String, String> formattedPrices = new HashMap<>();
            DecimalFormat df = new DecimalFormat("0.00");
            for (Producto p : productos) {
                String precioFormateado = df.format(p.getPrecio()) + " €";
                formattedPrices.put(p.getCodigo(), precioFormateado);
            }
            model.addAttribute("formattedPrices", formattedPrices);
            // -----------------------------------------------------------------------

            Map<String, Integer> Listadisponibles = new HashMap<>();
            for (Producto p : productos) {
                int disponibles = S_stock.obtenerStockTotalPorProducto(p);
                Listadisponibles.put(p.getCodigo(), disponibles);
            }
            model.addAttribute("disponibles", Listadisponibles);
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la información: " + e.getMessage());
        }

        return "inicio-cliente";
    }

	@PostMapping("/productos-favoritos")
    public String anadirProductoFavorito(@RequestParam("CodigoProducto") String CodigoProducto,
                                        @ModelAttribute("id_Cliente") long id_Cliente,
                                        Model model) {

        Optional <Producto> productoFavoritoOpt = S_producto.buscarProductoPorId(CodigoProducto);
        if (productoFavoritoOpt.isEmpty()) {
            model.addAttribute("error", "Producto no encontrado.");
            return "redirect:/inicio-cliente";
        }
        Producto productoSeleccionado = productoFavoritoOpt.get();

        Optional<Cliente> Cliente = S_cliente.buscarPorId(id_Cliente);
        if (Cliente.isEmpty()) {
            model.addAttribute("error", "Cliente no encontrado.");
            return "redirect:/inicio-cliente";
        }
        Cliente cliente = Cliente.get();

        cliente.addProducto(productoSeleccionado);
        S_cliente.guardarProductosFavoritosCliente(cliente);

        model.addAttribute("mensaje", "Has añadido el producto " + productoSeleccionado.getNombre() + " a tus favoritas.");

        return "redirect:/inicio-cliente";
    }

    @PostMapping("/eliminar-favorito")
    public String eliminarProductoFavorito(@RequestParam("CodigoProducto") String CodigoProducto,
                                           @ModelAttribute("id_Cliente") long id_Cliente,
                                           RedirectAttributes redirectAttributes) {
        Optional<Producto> productoOpt = S_producto.buscarProductoPorId(CodigoProducto);
        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/inicio-cliente";
        }
        Producto producto = productoOpt.get();

        Optional<Cliente> clienteOpt = S_cliente.buscarPorId(id_Cliente);
        if (clienteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/inicio-cliente";
        }
        Cliente cliente = clienteOpt.get();

        cliente.removeProducto(producto);
        S_cliente.guardarProductosFavoritosCliente(cliente);

        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado de favoritos.");
        return "redirect:/inicio-cliente";
    }

	@GetMapping("/factura")
	public String facturaCompraCliente(@RequestParam("idPedido") Long idPedido,
	                                   @ModelAttribute("nombreUsuario") String nombreUsuario,
	                                   @ModelAttribute("id_Cliente") Long id_Cliente,
	                                   @ModelAttribute("UsuarioCliente") Cliente Usuario,
	                                   Model model) {

	    // Obtener pedido confirmado y sus items para construir la factura
	    Optional<Pedido> pedidoOpt = S_pedido.buscarPedidoPorId(idPedido);
	    if (pedidoOpt.isEmpty()) {
	        model.addAttribute("error", "Pedido no encontrado.");
	        return "redirect:/inicio-cliente";
	    }
	    Pedido pedidoDb = pedidoOpt.get();
	    Map<Producto, Long> productosConCantidad = pedidoDb.getItems().stream()
	            .collect(Collectors.groupingBy(PedidoItem::getProducto, Collectors.counting()));

	    model.addAttribute("mensaje", "Factura del cliente");
	    model.addAttribute("UsuarioActual", nombreUsuario);
	    model.addAttribute("id_Cliente", id_Cliente);
	    model.addAttribute("UsuarioCliente", Usuario);
	    model.addAttribute("fechaHoy", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	    model.addAttribute("direccionCliente", Usuario.getDireccionEnvio());
	    model.addAttribute("nifnieCliente", Usuario.getNif_nie());
	    model.addAttribute("emailCliente", Usuario.getEmail());
	    model.addAttribute("idPedido", idPedido);
	    model.addAttribute("productosConCantidad", productosConCantidad);

	    return "factura";
	}

	
	@GetMapping("/carrito-compra")
	public String carritoCompraCliente(@ModelAttribute("nombreUsuario") String nombreUsuario,
	                                   @ModelAttribute("id_Cliente") Long id_Cliente,
	                                   HttpSession session,
	                                   Model model) {
	    limpiarCarrito(session);
	    
	    model.addAttribute("mensaje", "Factura del cliente");
	    model.addAttribute("UsuarioActual", nombreUsuario);
	    model.addAttribute("id_Cliente", id_Cliente);

	    Pedido carrito = (Pedido) session.getAttribute("carrito");
	    model.addAttribute("carrito", carrito);

	    // --- CALCULAR TOTALES: subtotal, IVA y total ---
	    try {
	        BigDecimal subtotal = BigDecimal.ZERO;
	        if (carrito != null && carrito.getItems() != null) {
	            for (PedidoItem it : carrito.getItems()) {
	                BigDecimal precio = it.getPrecioUnitario() != null ? it.getPrecioUnitario() : BigDecimal.ZERO;
	                BigDecimal cantidad = BigDecimal.valueOf(it.getCantidad());
	                subtotal = subtotal.add(precio.multiply(cantidad));
	            }
	        }
	        // Supongo IVA 21% (ajustable)
	        BigDecimal ivaRate = BigDecimal.valueOf(0.21);
	        BigDecimal ivaAmount = subtotal.multiply(ivaRate).setScale(2, BigDecimal.ROUND_HALF_UP);
	        BigDecimal total = subtotal.add(ivaAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

	        DecimalFormat df = new DecimalFormat("0.00");
	        model.addAttribute("subtotalFormatted", df.format(subtotal) + " €");
	        model.addAttribute("ivaFormatted", df.format(ivaAmount) + " €");
	        model.addAttribute("totalFormatted", df.format(total) + " €");

	        // también expongo valores numéricos por si se necesitan en JS
	        model.addAttribute("subtotal", subtotal);
	        model.addAttribute("ivaAmount", ivaAmount);
	        model.addAttribute("total", total);
	    } catch (Exception e) {
	        model.addAttribute("subtotalFormatted", "0.00 €");
	        model.addAttribute("ivaFormatted", "0.00 €");
	        model.addAttribute("totalFormatted", "0.00 €");
	    }
	    // ------------------------------------------------

	    return "carrito-compra";
	}


	@PostMapping("/carrito-compra")
	public String anadirAlCarrito(@RequestParam("CodigoProducto") String codigoProducto,
                                        @RequestParam("cantidad") int cantidad,
                                        HttpSession session,
                                        Model model) {

	    Optional<Producto> productoOpt = S_producto.buscarProductoPorId(codigoProducto);
	    if (productoOpt.isEmpty()) {
	        model.addAttribute("error", "Producto no encontrado.");
	        return "redirect:/inicio-cliente";
	    }
	    Producto producto = productoOpt.get();

	    // comprobar disponibilidad global
	    if (!S_stock.reservarStock(producto, cantidad)) {
	        model.addAttribute("error", "No hay suficiente stock para ese producto.");
	        return "redirect:/inicio-cliente";
	    }

	    Pedido carrito = (Pedido) session.getAttribute("carrito");
	    if (carrito == null) {
	        carrito = new Pedido();
	        carrito.setFechaPedido(LocalDate.now());
	        session.setAttribute("carrito", carrito);
	    }

	    // buscar item existente
	    List<PedidoItem> items = carrito.getItems();
	    Optional<PedidoItem> exist = items.stream()
	            .filter(i -> i.getProducto()!=null && producto.getCodigo().equals(i.getProducto().getCodigo()))
	            .findFirst();
	    if (exist.isPresent()) {
	        PedidoItem it = exist.get();
	        int nuevaCantidad = it.getCantidad() + cantidad;
	        if (!S_stock.reservarStock(producto, nuevaCantidad)) {
	            model.addAttribute("error", "No hay suficiente stock para aumentar la cantidad solicitada.");
	            return "redirect:/inicio-cliente";
	        }
	        it.setCantidad(nuevaCantidad);
	    } else {
	        PedidoItem nuevo = new PedidoItem(producto, cantidad, BigDecimal.valueOf(producto.getPrecio()), es.educastur.oms.modelo.EstadoPedidoItem.RESERVADO);
	        carrito.addItem(nuevo);
	    }
	    session.setAttribute("carrito", carrito);

	    return "redirect:/carrito-compra";
	}


	@PostMapping("/eliminar-ejemplar")
	public String eliminarEjemplarDelCarrito(@RequestParam("codigoProducto") String codigoProducto,
	                                          HttpSession session,
	                                          RedirectAttributes redirectAttributes) {

	    Pedido carrito = (Pedido) session.getAttribute("carrito");
	    if (carrito == null) {
	        redirectAttributes.addFlashAttribute("error", "No hay carrito activo.");
	        return "redirect:/carrito-compra";
	    }

	    // Ahora manejamos el carrito por items (producto+cantidad)
	    // Eliminamos el item cuyo producto tenga el código recibido
	    carrito.getItems().removeIf(i -> i.getProducto() != null && codigoProducto.equals(i.getProducto().getCodigo()));

	    session.setAttribute("carrito", carrito);
	    redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado correctamente.");

	    return "redirect:/carrito-compra";
	}


	@PostMapping("/confirmar-pedido")
	public String confirmarPedido(HttpSession session, 
	                              @ModelAttribute("id_Cliente") Long id_Cliente,
	                              RedirectAttributes redirectAttributes) {
	    Pedido carrito = (Pedido) session.getAttribute("carrito");

	    if (carrito == null || carrito.getItems().isEmpty()) {
	        redirectAttributes.addFlashAttribute("error", "El carrito está vacío.");
	        return "redirect:/carrito-compra";
	    }

	    Optional<Cliente> cliente = S_cliente.buscarPorId(id_Cliente);
	    if (!cliente.isPresent()) {
	        redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
	        return "redirect:/carrito-compra";
	    }
	    Cliente clienteActual = cliente.get();

	    // Verificar reservas y consumir stock en una transacción lógica
	    try {
	        // crear pedido y asignar items
	        Pedido pedidoCliente = new Pedido();
	        pedidoCliente.setCliente(clienteActual);
	        pedidoCliente.setEstado(EstadoPedido.CONFIRMADO);
	        pedidoCliente.setFechaPedido(LocalDate.now());

	        // transferir items del carrito al pedido
	        for (PedidoItem item : carrito.getItems()) {
	            pedidoCliente.addItem(item);
	        }

	        // delegar confirmación y consumo en el servicio (transaccional)
	        Pedido pedidoConfirmado = S_pedido.confirmarPedido(pedidoCliente);
	        Long idPedido = pedidoConfirmado.getId();

	        session.removeAttribute("carrito");
	        redirectAttributes.addFlashAttribute("success", "Pedido realizado con éxito.");
	        return "redirect:/factura";
	    } catch (IllegalStateException ex) {
	        redirectAttributes.addFlashAttribute("error", "Error al procesar el pedido: " + ex.getMessage());
	        return "redirect:/carrito-compra";
	    }
 }

	@GetMapping("/mispedidos")
    public String pedidosCliente(@RequestParam(value = "estado", required = false, defaultValue = "todos") String estado,
                                 @ModelAttribute("nombreUsuario") String nombreUsuario,
                                 @ModelAttribute("UsuarioCliente") Cliente cliente,
                                 Model model) {
        List<Pedido> pedidos;
        if ("todos".equalsIgnoreCase(estado)) {
            pedidos = S_pedido.findByCliente(cliente);
            model.addAttribute("pedidos", pedidos);
        } else {
            // Filtrar por cliente y estado para no devolver pedidos de otros clientes
            EstadoPedido estadoEnum = EstadoPedido.valueOf(estado);
            pedidos = S_pedido.findByClienteAndEstado(cliente, estadoEnum);
            model.addAttribute("pedidos", pedidos);
        }

        // Calcular totales globales para el resumen: nº artículos e importe total
        int totalArticulos = 0;
        java.math.BigDecimal totalImporte = java.math.BigDecimal.ZERO;
        try {
            for (Pedido p : pedidos) {
                if (p.getItems() != null) {
                    for (PedidoItem it : p.getItems()) {
                        int cant = it.getCantidad();
                        totalArticulos += cant;
                        java.math.BigDecimal precio = it.getPrecioUnitario() != null ? it.getPrecioUnitario() : java.math.BigDecimal.ZERO;
                        totalImporte = totalImporte.add(precio.multiply(java.math.BigDecimal.valueOf(cant)));
                    }
                }
            }
        } catch (Exception e) {
            // en caso de problemas, dejamos valores por defecto
            totalArticulos = totalArticulos;
        }

        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        model.addAttribute("totalArticulos", totalArticulos);
        model.addAttribute("totalImporte", df.format(totalImporte) + " €");

        model.addAttribute("UsuarioActual", nombreUsuario);
        model.addAttribute("estados", EstadoPedido.values());
        model.addAttribute("estadoSeleccionado", estado);
        return "mispedidos";
    }

	@PostMapping("/cancelar-pedido") 
	public String cancelarPedido(@RequestParam("pedidoId") Long pedidoId, Model model) {
	    Optional<Pedido> pedido = S_pedido.buscarPedidoPorId(pedidoId);
	    
	    if (pedido.isPresent()) {
	        Pedido pedidoCliente = pedido.get();

	        if (!pedidoCliente.getEstado().equals(EstadoPedido.ENTREGADO)
	            && !pedidoCliente.getEstado().equals(EstadoPedido.COMPLETADO)) {

	            // Delegar en el servicio la modificación de estado para que incluya
	            // la lógica de reposición de stock cuando corresponda.
	            S_pedido.modificarEstadoPedido(pedidoId, EstadoPedido.CANCELADO);
	        }
	    }

	    return "redirect:/mispedidos";
	}
	
	/**
	 * Elimina del carrito los ejemplares que ya no están disponibles en la base de datos.
	 * Se ejecuta cada vez que se carga el carrito para evitar conflictos con otros pedidos.
	 * 
	 * @param session Sesión del usuario donde se almacena el carrito.
	 */
	private void limpiarCarrito(HttpSession session) {
	    Pedido carrito = (Pedido) session.getAttribute("carrito");
	    if (carrito != null) {
            // limpiar items con stock insuficiente
            if (carrito.getItems() != null && !carrito.getItems().isEmpty()) {
                List<PedidoItem> itemsActualizados = carrito.getItems().stream()
                        .filter(i -> S_stock.obtenerStockTotalPorProducto(i.getProducto()) >= i.getCantidad())
                        .collect(Collectors.toList());
                carrito.setItems(itemsActualizados);
            }
            // no hay compatibilidad con ejemplares: usamos items en el carrito
            session.setAttribute("carrito", carrito);
        }
    }
}
