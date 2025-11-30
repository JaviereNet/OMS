package es.educastur.oms.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.educastur.oms.modelo.PedidoItem;
import es.educastur.oms.modelo.Pedido;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
    List<PedidoItem> findByPedido(Pedido pedido);
}

