package es.educastur.oms.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.educastur.oms.modelo.Cliente;
import es.educastur.oms.modelo.EstadoPedido;
import es.educastur.oms.modelo.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	
	List<Pedido> findByEstado(EstadoPedido estado);
	List<Pedido> findByCliente(Cliente cliente); 
	List<Pedido> findByClienteAndEstado(Cliente cliente, EstadoPedido estado);
}
