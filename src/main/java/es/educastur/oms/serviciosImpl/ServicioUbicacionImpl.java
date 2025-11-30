package es.educastur.oms.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.educastur.oms.modelo.Ubicacion;
import es.educastur.oms.repositorios.UbicacionRepository;
import es.educastur.oms.servicios.ServicioUbicacion;


@Service
public class ServicioUbicacionImpl implements ServicioUbicacion{

    @Autowired
    private UbicacionRepository ubicacion_R;

    @Override
    public Ubicacion guardarUbicacion(Ubicacion ubicacion) {
        return ubicacion_R.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> obtenerUbicacionPorId(Long id) {
        return ubicacion_R.findById(id);
    }

    @Override
    public List<Ubicacion> obtenerTodasLasUbicacion() {
        return ubicacion_R.findAll();
    }

    @Override
    public void eliminarUbicacion(Long id) {
        ubicacion_R.deleteById(id);

    }

    @Override
    public List<Ubicacion> obtenerUbicacionesSinEjemplar() {
        return ubicacion_R.findUbicacionesSinEjemplar();
    }

}
