package es.educastur.oms.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.educastur.oms.modelo.Historial;
import java.util.Optional;

/**
 * Repositorio para la entidad Historial.
 */
@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long>{

    /**
     * Buscar historial por id de pedido (FK id_pedido en tabla historial)
     */
    Optional<Historial> findByPedidoId(Long pedidoId);

}
