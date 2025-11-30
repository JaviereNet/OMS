package es.educastur.oms.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import es.educastur.oms.modelo.Cliente;
import es.educastur.oms.modelo.Producto;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	boolean existsByNombre(String nombre);
	
	@Query("SELECT c.productosFavoritos FROM Cliente c WHERE c.id_cliente = :idCliente")
	List<Producto> findProductosByClienteId(@Param("idCliente") Long idCliente);

	@Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.email = :email")
    boolean existeClientePorEmail(@Param("email") String email);	
	
	@Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.nif_nie = :nif_nie")
    boolean existeClientePorNifNie(@Param("nif_nie") String nif_nie);
}
