package es.educastur.oms.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import es.educastur.oms.modelo.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT COUNT(p) > 0 FROM Producto p WHERE p.codigo = :codigo")
    boolean existProductoPorCodigo(@Param("codigo") String codigo);
    
    @Query("SELECT COUNT(p) FROM Producto p")
    long contarProductos();

    // Consulta explícita para evitar ambigüedades y forzar búsqueda por el campo codigo (String)
    @Query("SELECT p FROM Producto p WHERE p.codigo = :codigo")
    Optional<Producto> findByCodigo(@Param("codigo") String codigo);
}
