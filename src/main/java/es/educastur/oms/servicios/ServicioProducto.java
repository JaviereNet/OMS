package es.educastur.oms.servicios;

import java.util.List;
import java.util.Optional;
import es.educastur.oms.modelo.Producto;

public interface ServicioProducto {

    Producto guardarProducto(Producto producto);
    Producto modificarProducto(Producto producto);
    Optional<Producto> buscarProductoPorId(String codigo);
    List<Producto> listarProductos();
    long contadorProductos();
    void eliminarProducto(String codigo);
    boolean tieneEjemplaresAsociados(String codigo);
    boolean existProductoPorCodigo(String codigo);
}
