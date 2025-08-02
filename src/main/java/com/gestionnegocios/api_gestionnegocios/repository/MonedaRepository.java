package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Moneda.
 */
@Repository
public interface MonedaRepository extends JpaRepository<Moneda, Integer> {
    Optional<Moneda> findByNombre(String nombre);
}
