package es.educastur.oms.controlador;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.educastur.oms.servicios.ServicioPedido;
import es.educastur.oms.servicios.ServicioStock;
import es.educastur.oms.modelo.EstadoPedido;
import es.educastur.oms.modelo.Pedido;
import es.educastur.oms.modelo.PedidoItem;
import es.educastur.oms.modelo.Stock;

@Controller
@SessionAttributes({"nombreUsuario", "id_Persona", "id_Cliente", "UsuarioCliente", "UsuarioPersona"})
public class PedidoController {

    @Autowired
    private ServicioPedido S_pedido;

    // Servicio para obtener filas de stock y sus ubicaciones (solo lectura aquí)
    @Autowired
    private ServicioStock S_stock;

    @GetMapping("/gestion-pedidos")
    public String gestionPedidos(@RequestParam(value = "estado", required = false, defaultValue = "todos") String estado,
                                 @ModelAttribute("nombreUsuario") String nombreUsuario,
                                 Model model) {
        List<Pedido> lista;
        if ("todos".equalsIgnoreCase(estado)) {
            lista = S_pedido.listarPedidos();
        } else {
            lista = S_pedido.findByEstado(EstadoPedido.valueOf(estado));
        }

        // Rellenar ubicaciones en los items para visualización cuando falten
        asignarUbicacionesParaVisualizacion(lista);

        model.addAttribute("pedidos", lista);
        model.addAttribute("UsuarioActual", nombreUsuario);
        model.addAttribute("estados", EstadoPedido.values());
        model.addAttribute("estadoSeleccionado", estado);
        return "gestion-pedidos";
    }


    /**
     * Modifica el estado de un pedido según la solicitud del usuario.
     *   
     * Si el nuevo estado no es "CANCELADO", simplemente actualiza el estado del pedido.  
     * Si el nuevo estado es "CANCELADO", además de actualizar el estado,  
     * se liberan los ejemplares asociados al pedido, marcándolos como disponibles  
     * y desvinculándolos del pedido (estableciendo su atributo pedido en null).  
     * Finalmente, el pedido se guarda con el nuevo estado.  
     *
     * @param pedidoId    ID del pedido que se quiere modificar.
     * @param nuevoEstado Nuevo estado que se asignará al pedido.
     * @param model       Modelo de la vista para la gestión de pedidos.
     * @return Redirecciona a la página de gestión de pedidos después de la modificación.
     */
    @PostMapping("/modificar-estado")
    public String modificarEstado(@RequestParam("pedidoId") Long pedidoId, 
                                  @RequestParam("nuevoEstado") EstadoPedido nuevoEstado,
                                  Model model) {
        
        Optional<Pedido> pedido = S_pedido.buscarPedidoPorId(pedidoId);
        
        if (pedido.isPresent()) {
            Pedido pedidoCliente = pedido.get();

            if (nuevoEstado != EstadoPedido.CANCELADO) {
                S_pedido.modificarEstadoPedido(pedidoId, nuevoEstado);
            } else {
                // Delegamos en el servicio para que haga la reposición de stock si aplica
                S_pedido.modificarEstadoPedido(pedidoId, EstadoPedido.CANCELADO);
            }
        }

        return "redirect:/gestion-pedidos";
    }

    /**
     * Para mostrar ubicaciones en la vista: si un PedidoItem no tiene ubicacion asignada
     * intentamos obtener la primera fila de Stock disponible para el producto y usar
     * su Ubicacion solo para visualización (no se persiste aquí).
     */
    private void asignarUbicacionesParaVisualizacion(List<Pedido> pedidos) {
        if (pedidos == null) return;
        for (Pedido p : pedidos) {
            if (p.getItems() == null) continue;
            for (PedidoItem item : p.getItems()) {
                try {
                    // Si no tiene ubicacion, intentamos asignar la primera disponible
                    if (item.getUbicacion() == null && item.getProducto() != null) {
                        List<Stock> stocks = S_stock.listarStockPorProducto(item.getProducto());
                        if (stocks != null && !stocks.isEmpty()) {
                            // usar la primera ubicacion encontrada para mostrar
                            item.setUbicacion(stocks.get(0).getUbicacion());
                        }
                    }
                    // Forzamos inicialización de la entidad Ubicacion para evitar proxies no inicializados
                    if (item.getUbicacion() != null) {
                        // acceder al codigo inicializa el proxy y asegura que Thymeleaf pueda leerlo
                        try {
                            item.getUbicacion().getCodigo();
                        } catch (Exception ignore) {
                            // ignorar problemas de inicialización; no queremos romper la vista
                        }
                    }
                } catch (Exception ex) {
                    // No propagamos errores, sólo queremos mejorar la vista
                }
            }
        }
    }
}