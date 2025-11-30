package es.educastur.oms.servicios;

import java.util.List;
import java.util.Optional;
import es.educastur.oms.modelo.Cliente;
import es.educastur.oms.modelo.Producto;

//--------------------------------------------------------
//Autor: Javier García Ramos
//Fecha: 2025-02-13
//Descripción: Interfaz de servicio para gestionar las operaciones
//relacionadas con los Clientes del almacén. Proporciono métodos
//para listar, buscar, guardar, guardar sus productos favoritos y eliminar.
//--------------------------------------------------------
public interface ServicioCliente {
	
	List<Cliente> listarClientes();
    Optional<Cliente> buscarPorId(Long id);
    Cliente guardarCliente(Cliente cliente);
    
    /**
     * Guarda los productos favoritos de un cliente.
     *
     * @param cliente Cliente con los productos favoritos actualizados.
     * @return Cliente actualizado.
     */
    Cliente guardarProductosFavoritosCliente(Cliente cliente);
    void eliminarCliente(Long id);
    List<Producto> findProductosByClienteId(Long id_Cliente);
    boolean existClientePorEmail(String email);
    boolean existClientePorNifNie(String nif_nie);
}
