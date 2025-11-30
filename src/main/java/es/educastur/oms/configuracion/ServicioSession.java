package es.educastur.oms.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServicioSession {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void eliminarSesionesPrevias(String username) {
        String sql = "DELETE FROM SPRING_SESSION WHERE PRINCIPAL_NAME = ?";
        jdbcTemplate.update(sql, username);
        System.out.println("Se eliminaron sesiones previas de: " + username);
    }
}
