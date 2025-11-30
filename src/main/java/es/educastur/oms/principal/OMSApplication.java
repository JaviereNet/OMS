package es.educastur.oms.principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "es.educastur.oms")
@EntityScan(basePackages = "es.educastur.oms.modelo")
@EnableJpaRepositories(basePackages = "es.educastur.oms.repositorios")
public class OMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(OMSApplication.class, args);
    }
}