package es.educastur.oms.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.educastur.oms.modelo.Producto;
import es.educastur.oms.repositorios.ProductoRepository;
import es.educastur.oms.repositorios.StockRepository;
import es.educastur.oms.servicios.ServicioProducto;

@Service
public class ServicioProductoImpl implements ServicioProducto {
    @Autowired
    private ProductoRepository producto_R;
    
    @Autowired
    private StockRepository stock_R;

    @Override
    public Producto guardarProducto(Producto producto) {
        return producto_R.saveAndFlush(producto);
    }

    @Override
    public Producto modificarProducto(Producto producto) {
        return producto_R.save(producto);
    }

    @Override
    public Optional<Producto> buscarProductoPorId(String codigo) {
        if (codigo == null) {
            return Optional.empty();
        }
        // Preferimos delegar en la consulta explícita del repositorio para
        // evitar recorrer toda la lista en memoria y para que JPA resuelva
        // correctamente la búsqueda por el campo `codigo` (String).
        return producto_R.findByCodigo(codigo);
        // Nota: la alternativa con findAll().stream() se eliminó porque provocaba
        // confusiones en entornos donde se intentaba interpretar el código
        // como clave primaria (Long).
    }

    @Override
    public List<Producto> listarProductos() {
        return producto_R.findAll();
    }
    
    public long contadorProductos() {
        return producto_R.contarProductos();
    }

    @Override
    public void eliminarProducto(String codigo) {
        Optional<Producto> opt = producto_R.findByCodigo(codigo);
        opt.ifPresent(p -> producto_R.deleteById(p.getId()));
    }
    
    @Override
    public boolean tieneEjemplaresAsociados(String codigo) {
        // Comprobamos si existe stock asociado al producto
        Optional<Producto> pOpt = producto_R.findByCodigo(codigo);
        if (pOpt.isEmpty()) return false;
        Integer total = stock_R.sumCantidadByProducto(pOpt.get());
        return total != null && total > 0;
    }

    public boolean existProductoPorCodigo(String codigo) {
        return producto_R.existProductoPorCodigo(codigo);
    }
}
