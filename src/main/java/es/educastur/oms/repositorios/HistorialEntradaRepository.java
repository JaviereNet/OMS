package es.educastur.oms.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.educastur.oms.modelo.HistorialEntrada;

@Repository
public interface HistorialEntradaRepository extends JpaRepository<HistorialEntrada, Long> {

}

