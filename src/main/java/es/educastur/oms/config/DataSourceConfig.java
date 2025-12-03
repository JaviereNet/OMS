package es.educastur.oms.config;

import java.net.URI;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @Primary
    public DataSource dataSource(Environment env) {
        String configured = env.getProperty("spring.datasource.url");
        if (configured != null && !configured.isBlank()) {
            log.info("Usando spring.datasource.url: {}", (configured.length() > 60 ? configured.substring(0, 60) + "..." : configured));
            return DataSourceBuilder.create()
                    .driverClassName(env.getProperty("spring.datasource.driver-class-name", "org.postgresql.Driver"))
                    .url(configured)
                    .username(env.getProperty("spring.datasource.username"))
                    .password(env.getProperty("spring.datasource.password"))
                    .build();
        }

        // Buscar otras variables de entorno
        String url = env.getProperty("SPRING_DATASOURCE_URL");
        if (isEmpty(url)) url = env.getProperty("DB_URL");
        if (isEmpty(url)) url = env.getProperty("DATABASE_URL");

        if (isEmpty(url)) {
            log.warn("No se encontró URL de base de datos en variables de entorno (spring.datasource.url, SPRING_DATASOURCE_URL, DB_URL, DATABASE_URL). Dejar que Spring gestione el DataSource por defecto.");
            return null;
        }

        // Si la URL no es JDBC, intentar convertir (postgresql://... -> jdbc:postgresql://...)
        String jdbcUrl = url;
        String username = env.getProperty("SPRING_DATASOURCE_USERNAME", env.getProperty("DB_USERNAME"));
        String password = env.getProperty("SPRING_DATASOURCE_PASSWORD", env.getProperty("DB_PASSWORD"));

        if (!url.startsWith("jdbc:")) {
            try {
                // soporta formatos como postgresql://user:pass@host:port/db
                URI uri = new URI(url);
                String userInfo = uri.getUserInfo();
                if (userInfo != null && (username == null || username.isBlank())) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    if (parts.length > 1) password = parts[1];
                }
                String host = uri.getHost();
                int port = uri.getPort();
                String path = uri.getPath();
                if (path != null && path.startsWith("/")) path = path.substring(1);
                jdbcUrl = "jdbc:postgresql://" + host + (port == -1 ? "" : ":" + port) + "/" + (path == null ? "" : path);
                log.info("Convertida URL a JDBC: {}", jdbcUrl);
            } catch (Exception ex) {
                log.error("No se pudo parsear DATABASE_URL/DB_URL: {}", ex.getMessage());
                throw new IllegalStateException("URL de BD inválida: " + url, ex);
            }
        }

        DataSourceBuilder<?> dsb = DataSourceBuilder.create()
                .driverClassName(env.getProperty("spring.datasource.driver-class-name", "org.postgresql.Driver"))
                .url(jdbcUrl);
        if (username != null && !username.isBlank()) dsb.username(username);
        if (password != null && !password.isBlank()) dsb.password(password);

        log.info("Construyendo DataSource con URL {}", (jdbcUrl.length() > 60 ? jdbcUrl.substring(0, 60) + "..." : jdbcUrl));
        return dsb.build();
    }

    private boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }
}

