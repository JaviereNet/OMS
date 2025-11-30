package es.educastur.oms.serviciosImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.educastur.oms.modelo.Stock;
import es.educastur.oms.modelo.Producto;
import es.educastur.oms.modelo.Ubicacion;
import es.educastur.oms.repositorios.StockRepository;
import es.educastur.oms.servicios.ServicioStock;

@Service
public class ServicioStockImpl implements ServicioStock {

    @Autowired
    private StockRepository stock_R;

    @Override
    public Stock guardarStock(Stock stock) {
        return stock_R.save(stock);
    }

    @Override
    public Optional<Stock> obtenerStockPorId(Long id) {
        return stock_R.findById(id);
    }

    @Override
    public List<Stock> listarStockPorProducto(Producto producto) {
        return stock_R.findByProducto(producto);
    }

    @Override
    public List<Stock> listarStockPorUbicacion(Ubicacion ubicacion) {
        return stock_R.findByUbicacion(ubicacion);
    }

    @Override
    public Stock obtenerStockPorProductoYUbicacion(Producto producto, Ubicacion ubicacion) {
        return stock_R.findByProductoAndUbicacion(producto, ubicacion);
    }

    @Override
    public int obtenerStockTotalPorProducto(Producto producto) {
        Integer total = stock_R.sumCantidadByProducto(producto);
        return (total != null) ? total : 0;
    }

    @Override
    public boolean reservarStock(Producto producto, int cantidad) {
        int total = obtenerStockTotalPorProducto(producto);
        return total >= cantidad;
    }

    @Override
    public void consumirStock(Producto producto, int cantidad) {
        int remaining = cantidad;
        List<Stock> stocks = stock_R.findByProducto(producto);
        if (stocks == null || stocks.isEmpty()) {
            throw new IllegalStateException("No hay stock para el producto: " + producto.getCodigo());
        }
        for (Stock s : stocks) {
            if (remaining <= 0) break;
            int available = s.getCantidad();
            if (available <= 0) continue;
            int toTake = Math.min(available, remaining);
            s.setCantidad(available - toTake);
            stock_R.save(s);
            remaining -= toTake;
        }
        if (remaining > 0) {
            throw new IllegalStateException("Stock insuficiente al intentar consumir producto: " + producto.getCodigo());
        }
    }

    @Override
    public java.util.List<es.educastur.oms.modelo.AsignacionStockUbicacion> consumirStockConAsignacion(Producto producto, int cantidad) {
        int remaining = cantidad;
        List<Stock> stocks = stock_R.findByProducto(producto);
        if (stocks == null || stocks.isEmpty()) {
            throw new IllegalStateException("No hay stock para el producto: " + producto.getCodigo());
        }
        java.util.List<es.educastur.oms.modelo.AsignacionStockUbicacion> asignaciones = new java.util.ArrayList<>();
        for (Stock s : stocks) {
            if (remaining <= 0) break;
            int available = s.getCantidad();
            if (available <= 0) continue;
            int toTake = Math.min(available, remaining);
            s.setCantidad(available - toTake);
            stock_R.save(s);
            remaining -= toTake;
            asignaciones.add(new es.educastur.oms.modelo.AsignacionStockUbicacion(s.getId(), s.getUbicacion(), toTake, s.getLote()));
        }
        if (remaining > 0) {
            throw new IllegalStateException("Stock insuficiente al intentar consumir producto: " + producto.getCodigo());
        }
        return asignaciones;
    }

    @Override
    public void reponerStockEnUbicacion(Producto producto, Ubicacion ubicacion, int cantidad) {
        if (cantidad <= 0) return;
        Stock s = stock_R.findByProductoAndUbicacion(producto, ubicacion);
        if (s != null) {
            s.setCantidad(s.getCantidad() + cantidad);
            stock_R.save(s);
            return;
        }
        Stock nuevo = new Stock();
        nuevo.setProducto(producto);
        nuevo.setUbicacion(ubicacion);
        nuevo.setCantidad(cantidad);
        nuevo.setLote(null);
        nuevo.setUnidad(null);
        stock_R.save(nuevo);
    }

    @Override
    public void reponerStock(Producto producto, int cantidad) {
        if (cantidad <= 0) return;
        List<Stock> stocks = stock_R.findByProducto(producto);
        if (stocks != null && !stocks.isEmpty()) {
            Stock s = stocks.get(0);
            s.setCantidad(s.getCantidad() + cantidad);
            stock_R.save(s);
            return;
        }
        Stock nuevo = new Stock();
        nuevo.setProducto(producto);
        nuevo.setCantidad(cantidad);
        nuevo.setLote(null);
        nuevo.setUnidad(null);
        stock_R.save(nuevo);
    }

    @Override
    public void eliminarStock(Long id) {
        stock_R.deleteById(id);
    }
}
