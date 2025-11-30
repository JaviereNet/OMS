package es.educastur.oms.servicios;

import java.util.List;
import java.util.Optional;
import es.educastur.oms.modelo.Ubicacion;

public interface ServicioUbicacion {

    Ubicacion guardarUbicacion(Ubicacion ubicacion);
    Optional<Ubicacion> obtenerUbicacionPorId(Long id);
    List<Ubicacion> obtenerUbicacionesSinEjemplar();
    List<Ubicacion> obtenerTodasLasUbicacion();
    void eliminarUbicacion(Long id);

}
