package es.educastur.oms.serviciosImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.educastur.oms.modelo.Cliente;
import es.educastur.oms.modelo.EstadoPedido;
import es.educastur.oms.modelo.Pedido;
import es.educastur.oms.repositorios.PedidoRepository;
import es.educastur.oms.modelo.EstadoPedidoItem;
import es.educastur.oms.servicios.ServicioPedido;
import es.educastur.oms.repositorios.HistorialRepository;
import es.educastur.oms.repositorios.HistorialEntradaRepository;
import es.educastur.oms.modelo.Historial;
import es.educastur.oms.modelo.HistorialEntrada;
import java.time.LocalDateTime;

@Service
public class ServicioPedidoImpl implements ServicioPedido{
	
	@Autowired
    private PedidoRepository pedido_R;

    @Autowired
    private es.educastur.oms.servicios.ServicioStock S_stock;

     @Autowired
     private HistorialRepository historial_R;

     @Autowired
     private HistorialEntradaRepository historialEntrada_R;

	@Override
	public Pedido guardarPedido(Pedido pedido) {
		return pedido_R.save(pedido);
	}

	@Override
	public Pedido modificarPedido(Pedido pedido) {
		return pedido_R.save(pedido);
	}

	@Override
	public Optional<Pedido> buscarPedidoPorId(Long id) {
		return pedido_R.findById(id);
	}

	@Override
	public List<Pedido> listarPedidos() {
		return pedido_R.findAll();
	}
	
	public List<Pedido> findByEstado(EstadoPedido estado) {
	    return pedido_R.findByEstado(estado);
	}

    @Override
    @Transactional
    public Pedido confirmarPedido(Pedido pedido) {
        // Validar disponibilidad global antes de consumir (reservar)
        for (var item : pedido.getItems()) {
            if (!S_stock.reservarStock(item.getProducto(), item.getCantidad())) {
                throw new IllegalStateException("Stock insuficiente para producto: " + item.getProducto().getCodigo());
            }
        }

        // Consumir stock y desglosar items por ubicacion (una linea por ubicacion utilizada)
        java.util.List<es.educastur.oms.modelo.PedidoItem> nuevasLineas = new java.util.ArrayList<>();
        for (var item : pedido.getItems()) {
            java.util.List<es.educastur.oms.modelo.AsignacionStockUbicacion> asigns = S_stock.consumirStockConAsignacion(item.getProducto(), item.getCantidad());
            for (var a : asigns) {
                es.educastur.oms.modelo.PedidoItem linea = new es.educastur.oms.modelo.PedidoItem();
                linea.setProducto(item.getProducto());
                linea.setCantidad(a.getCantidad());
                linea.setPrecioUnitario(item.getPrecioUnitario());
                linea.setEstado(EstadoPedidoItem.CONFIRMADO);
                linea.setUbicacion(a.getUbicacion());
                // lote/unidad si aplica
                linea.setLote(a.getLote());
                linea.setUnidad(item.getUnidad());
                linea.setPedido(pedido);
                nuevasLineas.add(linea);
            }
        }

        // Reemplazar las items originales por las nuevas lineas por ubicacion
        pedido.getItems().clear();
        pedido.getItems().addAll(nuevasLineas);

        return pedido_R.save(pedido);
    }

	@Override
	public void eliminarPedido(Pedido pedido) {
		pedido_R.delete(pedido);
		
	}

	@Override
	public boolean existPedidoPorId(Long id) {
		return pedido_R.existsById(id);
	}
	
	public void modificarEstadoPedido(Long id, EstadoPedido nuevoEstado) {
         Pedido pedido = pedido_R.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
         EstadoPedido antiguo = pedido.getEstado();
         pedido.setEstado(nuevoEstado);
         pedido_R.save(pedido);

         // Si el pedido pasa a CANCELADO y antes no estaba cancelado, devolver stock de los items.
         if (nuevoEstado == EstadoPedido.CANCELADO && antiguo != EstadoPedido.CANCELADO) {
             for (var item : pedido.getItems()) {
                 // Solo reponemos stock de items que estaban confirmados o reservados
                 if (item.getEstado() == EstadoPedidoItem.CONFIRMADO || item.getEstado() == EstadoPedidoItem.RESERVADO) {
                     if (item.getUbicacion() != null) {
                         S_stock.reponerStockEnUbicacion(item.getProducto(), item.getUbicacion(), item.getCantidad());
                     } else {
                         S_stock.reponerStock(item.getProducto(), item.getCantidad());
                     }
                     item.setEstado(EstadoPedidoItem.CANCELADO);
                 }
             }
             // Persistir cambios en items
             pedido_R.save(pedido);
         }

         // Registrar en Historial la entrada de cambio de estado
         Historial historial = historial_R.findByPedidoId(pedido.getId()).orElse(null);
         if (historial == null) {
             historial = new Historial();
             historial.setPedido(pedido);
             historial_R.save(historial);
         }

         HistorialEntrada entrada = new HistorialEntrada(LocalDateTime.now(), nuevoEstado.name(), "");
         entrada.setHistorial(historial);
         historialEntrada_R.save(entrada);
         // opcional: añadir a la lista en memoria
         historial.getEntradas().add(entrada);
         historial_R.save(historial);
     }

	@Override
	public List<Pedido> findByCliente(Cliente cliente) {
		return pedido_R.findByCliente(cliente);
	}

	@Override
	public List<Pedido> findByClienteAndEstado(Cliente cliente, EstadoPedido estado) {
		return pedido_R.findByClienteAndEstado(cliente, estado);
	}
}
