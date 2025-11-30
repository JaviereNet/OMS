package es.educastur.oms.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2025-02-17
//Descripción: Clase VO de pedidos.
//--------------------------------------------------------


@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

    @Column(name = "estado")
    private EstadoPedido estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items = new ArrayList<>();

    // helper methods
    public void addItem(PedidoItem item) {
        item.setPedido(this);
        this.items.add(item);
    }

    public void removeItem(PedidoItem item) {
        item.setPedido(null);
        this.items.remove(item);
    }

	public Pedido() {
		super();
	}
	
	public Pedido(long id, LocalDate fechaPedido, Cliente cliente, List<PedidoItem> items) {
		super();
		this.id = id;
		this.fechaPedido = fechaPedido;
		this.cliente = cliente;
		this.items = items;
	}

	public Pedido(long id, LocalDate fechaPedido, EstadoPedido estado, Cliente cliente, List<PedidoItem> items) {
		super();
		this.id = id;
		this.fechaPedido = fechaPedido;
		this.estado = estado;
		this.cliente = cliente;
		this.items = items;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(LocalDate fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public EstadoPedido getEstado() {
		return estado;
	}

	public void setEstado(EstadoPedido estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<PedidoItem> getItems() {
		return items;
	}

	public void setItems(List<PedidoItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", fechaPedido=" + fechaPedido + ", estado=" + estado + ", cliente=" + cliente
				+ ", items=" + items + "]";
	}
	
}
