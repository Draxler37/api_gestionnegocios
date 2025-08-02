package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Cuenta.
 */
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {
    List<Cuenta> findByNegocioId(Integer negocioId);
}
