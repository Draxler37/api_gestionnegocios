package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.MovimientoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad MovimientoLog.
 */
@Repository
public interface MovimientoLogRepository extends JpaRepository<MovimientoLog, Integer> {
    List<MovimientoLog> findByIdMovimiento(Integer idMovimiento);
}
