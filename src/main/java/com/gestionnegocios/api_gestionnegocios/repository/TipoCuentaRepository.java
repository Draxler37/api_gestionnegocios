package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.TipoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad TipoCuenta.
 */
@Repository
public interface TipoCuentaRepository extends JpaRepository<TipoCuenta, Integer> {
    Optional<TipoCuenta> findByNombre(String nombre);
}
