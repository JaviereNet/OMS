package es.educastur.oms.config;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUrlEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String existing = environment.getProperty("spring.datasource.url");
        if (existing != null && !existing.isBlank()) {
            log.debug("spring.datasource.url ya presente en el environment: {}", existing);
            return;
        }

        String url = environment.getProperty("SPRING_DATASOURCE_URL");
        if (url == null || url.isBlank()) url = environment.getProperty("DB_URL");
        if (url == null || url.isBlank()) url = environment.getProperty("DATABASE_URL");
        if (url == null || url.isBlank()) {
            log.debug("No se encontró DATABASE_URL/DB_URL/SPRING_DATASOURCE_URL en el environment");
            return;
        }

        String jdbcUrl = url;
        String username = environment.getProperty("SPRING_DATASOURCE_USERNAME");
        String password = environment.getProperty("SPRING_DATASOURCE_PASSWORD");

        if (!url.startsWith("jdbc:")) {
            try {
                URI uri = new URI(url);
                String host = uri.getHost();
                int port = uri.getPort();
                String path = uri.getPath();
                if (path != null && path.startsWith("/")) path = path.substring(1);
                jdbcUrl = "jdbc:postgresql://" + host + (port == -1 ? "" : ":" + port) + "/" + (path == null ? "" : path);

                String userInfo = uri.getUserInfo();
                if (userInfo != null && (username == null || username.isBlank())) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    if (parts.length > 1) password = parts[1];
                }
            } catch (Exception ex) {
                log.warn("No se pudo parsear DATABASE_URL '{}': {}", url, ex.getMessage());
                return;
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("spring.datasource.url", jdbcUrl);
        if (username != null && !username.isBlank()) map.put("spring.datasource.username", username);
        if (password != null && !password.isBlank()) map.put("spring.datasource.password", password);

        log.info("Setting spring.datasource.url from DATABASE_URL/DB_URL");
        MapPropertySource ps = new MapPropertySource("render-database-url", map);
        environment.getPropertySources().addFirst(ps);
    }
}

