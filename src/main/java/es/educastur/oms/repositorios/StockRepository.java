package es.educastur.oms.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import es.educastur.oms.modelo.Stock;
import es.educastur.oms.modelo.Producto;
import es.educastur.oms.modelo.Ubicacion;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByProducto(Producto producto);
    List<Stock> findByUbicacion(Ubicacion ubicacion);
    Stock findByProductoAndUbicacion(Producto producto, Ubicacion ubicacion);

    @Query("SELECT COALESCE(SUM(s.cantidad),0) FROM Stock s WHERE s.producto = :producto")
    Integer sumCantidadByProducto(@Param("producto") Producto producto);
}
