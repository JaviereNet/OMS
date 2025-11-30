package es.educastur.oms.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.educastur.oms.modelo.Stock;
import es.educastur.oms.modelo.Producto;
import es.educastur.oms.modelo.Ubicacion;
import es.educastur.oms.servicios.ServicioStock;
import es.educastur.oms.servicios.ServicioProducto;
import es.educastur.oms.servicios.ServicioUbicacion;

@Controller
public class StockController {

    @Autowired
    private ServicioStock S_stock;

    @Autowired
    private ServicioProducto S_producto;

    @Autowired
    private ServicioUbicacion S_ubicacion;

    @GetMapping("/stock")
    public String listadoStock(Model model) {
        model.addAttribute("stocks", S_stock.listarStockPorProducto(null));
        model.addAttribute("productos", S_producto.listarProductos());
        return "stock-lista";
    }

    @GetMapping("/stock/producto/{codigo}")
    public String stockPorProducto(@PathVariable("codigo") String codigo, Model model) {
        Producto producto = S_producto.buscarProductoPorId(codigo).orElse(null);
        List<Stock> stocks = (producto != null) ? S_stock.listarStockPorProducto(producto) : List.of();
        model.addAttribute("stocks", stocks);
        model.addAttribute("producto", producto);
        return "stock-lista-producto";
    }

}

