package es.educastur.oms.servicios;

import java.util.List;
import java.util.Optional;
import es.educastur.oms.modelo.Stock;
import es.educastur.oms.modelo.Producto;
import es.educastur.oms.modelo.Ubicacion;

public interface ServicioStock {
    Stock guardarStock(Stock stock);
    Optional<Stock> obtenerStockPorId(Long id);
    List<Stock> listarStockPorProducto(Producto producto);
    List<Stock> listarStockPorUbicacion(Ubicacion ubicacion);
    Stock obtenerStockPorProductoYUbicacion(Producto producto, Ubicacion ubicacion);
    /**
     * Devuelve la suma total de cantidades disponibles para un producto en todas las ubicaciones.
     */
    int obtenerStockTotalPorProducto(Producto producto);
    /**
     * Comprueba si hay al menos 'cantidad' disponible del producto (reserva lógica).
     */
    boolean reservarStock(Producto producto, int cantidad);
    /**
     * Consume físicamente 'cantidad' unidades del producto distribuidas entre ubicaciones.
     * Lanza IllegalStateException si no hay suficiente stock.
     */
    void consumirStock(Producto producto, int cantidad);
    void eliminarStock(Long id);
    /**
     * Reponer (añadir) cantidad de producto al stock. Distribuye en la primera ubicación encontrada
     * o crea un registro nuevo si no existe ninguno.
     */
    void reponerStock(Producto producto, int cantidad);

    /**
     * Consume físicamente 'cantidad' unidades del producto y devuelve una lista
     * de asignaciones por ubicación (cantidad extraída de cada Stock).
     */
    java.util.List<es.educastur.oms.modelo.AsignacionStockUbicacion> consumirStockConAsignacion(Producto producto, int cantidad);

    /**
     * Reponer cantidad en una ubicación específica (buscar stock por producto+ubicacion, si no existe crearla).
     */
    void reponerStockEnUbicacion(Producto producto, Ubicacion ubicacion, int cantidad);
}
