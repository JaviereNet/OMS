package es.educastur.oms.servicios;

import java.util.List;
import java.util.Optional;

import es.educastur.oms.modelo.Cliente;
import es.educastur.oms.modelo.EstadoPedido;
import es.educastur.oms.modelo.Pedido;

public interface ServicioPedido {
	
	Pedido guardarPedido(Pedido pedido);
	Pedido modificarPedido(Pedido pedido);
    Optional<Pedido> buscarPedidoPorId(Long id);
    List<Pedido> listarPedidos();
    List<Pedido> findByEstado(EstadoPedido estado);
    void eliminarPedido(Pedido pedido);
    boolean existPedidoPorId(Long id);
    void modificarEstadoPedido(Long id, EstadoPedido nuevoEstado);
    List<Pedido> findByCliente(Cliente cliente);
    List<Pedido> findByClienteAndEstado(Cliente cliente, EstadoPedido estado);

    /**
     * Confirma y persiste un pedido: consume stock y marca items como confirmados en una transacción.
     */
    Pedido confirmarPedido(Pedido pedido);

}
