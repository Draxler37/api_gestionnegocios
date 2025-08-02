package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad TipoMovimiento.
 */
@Repository
public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Integer> {
    Optional<TipoMovimiento> findByNombre(String nombre);
}
