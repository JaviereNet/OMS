package es.educastur.oms.modelo;

/**
 * Clase auxiliar que representa una cantidad tomada de una fila de stock concreta y su ubicación.
 * Se coloca en el paquete modelo para tratarla como un objeto de dominio ligero (no persistente).
 */
public class AsignacionStockUbicacion {
    private Long idStock;
    private Ubicacion ubicacion;
    private int cantidad;
    private String lote;

    public AsignacionStockUbicacion() {}

    public AsignacionStockUbicacion(Long idStock, Ubicacion ubicacion, int cantidad, String lote) {
        this.idStock = idStock;
        this.ubicacion = ubicacion;
        this.cantidad = cantidad;
        this.lote = lote;
    }

    public Long getIdStock() {
        return idStock;
    }

    public void setIdStock(Long idStock) {
        this.idStock = idStock;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    @Override
    public String toString() {
        return "AsignacionStockUbicacion{" + "idStock=" + idStock + ", ubicacion=" + (ubicacion != null ? ubicacion.getCodigo() : null) + ", cantidad=" + cantidad + ", lote=" + lote + '}';
    }
}

