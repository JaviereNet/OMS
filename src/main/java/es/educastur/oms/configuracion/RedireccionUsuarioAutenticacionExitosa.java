package es.educastur.oms.configuracion;

import jakarta.servlet.ServletException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@SessionAttributes({"nombreUsuario", "id_Persona", "id_Cliente", "UsuarioCliente", "UsuarioPersona"})
public class RedireccionUsuarioAutenticacionExitosa implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        DetallesUsuario detallesUsuario = (DetallesUsuario) authentication.getPrincipal();
        String username = detallesUsuario.getUsername();
        Long id_Persona = detallesUsuario.getIdPersona();
        Long id_Cliente = detallesUsuario.getIdCliente();

        HttpSession session = request.getSession();
        session.setAttribute("nombreUsuario", username);
        session.setAttribute("id_Persona", detallesUsuario.getIdPersona());
        session.setAttribute("id_Cliente", detallesUsuario.getIdCliente());
        session.setAttribute("UsuarioCliente", detallesUsuario.getCliente());
        session.setAttribute("UsuarioPersona", detallesUsuario.getPersona());
        
        //DATOS AL INICIO DE SESIÓN PERSONAS(ADMIN O PERSONAL)/CLIENTES
        System.out.println(username);
        System.out.println(id_Persona);
        System.out.println(id_Cliente);    
        System.out.println(detallesUsuario.getCliente());  
        System.out.println(detallesUsuario.getPersona());

        if ("admin".equalsIgnoreCase(username)) {
            response.sendRedirect(request.getContextPath() + "/inicio-admin");
        } else if (id_Cliente<=0 & !"admin".equalsIgnoreCase(username)) { 
            response.sendRedirect(request.getContextPath() + "/inicio-personal");
        } else {
            response.sendRedirect(request.getContextPath() + "/inicio-cliente");
        }
    }
}


