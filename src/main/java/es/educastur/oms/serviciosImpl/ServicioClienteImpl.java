package es.educastur.oms.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.educastur.oms.modelo.Cliente;
import es.educastur.oms.modelo.Producto;
import es.educastur.oms.repositorios.ClienteRepository;
import es.educastur.oms.servicios.ServicioCliente;

import jakarta.transaction.Transactional;

@Service
public class ServicioClienteImpl implements ServicioCliente{
	
	@Autowired
	private ClienteRepository cliente_R;

	@Override
	public List<Cliente> listarClientes() {
		return cliente_R.findAll();
	}

	@Override
	public Optional<Cliente> buscarPorId(Long id) {
		return cliente_R.findById(id);
	}

	@Override
	public Cliente guardarCliente(Cliente cliente) {		
		return cliente_R.save(cliente);
	}

	@Override
	public void eliminarCliente(Long id) {
		cliente_R.deleteById(id);

	}
	
	@Transactional
	public Cliente guardarProductosFavoritosCliente(Cliente cliente) {
		return cliente_R.save(cliente);
	}

	@Override
	public List<Producto> findProductosByClienteId(Long id_Cliente) {
		return cliente_R.findProductosByClienteId(id_Cliente);
	}

	@Override
	public boolean existClientePorEmail(String email) {
		return cliente_R.existeClientePorEmail(email);
	}

	@Override
	public boolean existClientePorNifNie(String nif_nie) {
		return cliente_R.existeClientePorNifNie(nif_nie);
	}
}
