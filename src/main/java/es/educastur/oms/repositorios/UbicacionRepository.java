package es.educastur.oms.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import es.educastur.oms.modelo.Ubicacion;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    // Buscar ubicaciones que no tienen stock positivo (equivalente a 'sin ejemplar')
    @Query("SELECT u FROM Ubicacion u WHERE NOT EXISTS (SELECT s FROM Stock s WHERE s.ubicacion = u AND s.cantidad > 0)")
    List<Ubicacion> findUbicacionesSinEjemplar();

}
