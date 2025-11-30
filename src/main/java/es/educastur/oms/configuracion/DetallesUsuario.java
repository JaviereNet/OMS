package es.educastur.oms.configuracion;

import es.educastur.oms.modelo.Credenciales;
import es.educastur.oms.modelo.Cliente;
import es.educastur.oms.modelo.Persona;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetallesUsuario implements UserDetails {
	private static final long serialVersionUID = 1L;
	private final Credenciales credenciales;

    public DetallesUsuario(Credenciales credenciales) {
        this.credenciales = credenciales;
    }
    
    public Long getIdPersona() {
        Persona p = credenciales.getPersona();
        return (p != null) ? p.getId() : -1L;
    }
    
    public Long getIdCliente() {
        Cliente c = credenciales.getCliente();
        return (c != null) ? c.getId_cliente() : -1L;
    }
    
    public Cliente getCliente() {
        return credenciales.getCliente();
    }
    
    public Persona getPersona() {
        return credenciales.getPersona();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        if ("admin".equalsIgnoreCase(credenciales.getUsuario())) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (getIdCliente() <= 0) {
            roles.add(new SimpleGrantedAuthority("ROLE_PERSONAL"));
        } else {
            roles.add(new SimpleGrantedAuthority("ROLE_CLIENTE"));
        }
        return roles;
    }


    @Override
    public String getPassword() {
        return credenciales.getPassword();
    }

    @Override
    public String getUsername() {
        return credenciales.getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}

