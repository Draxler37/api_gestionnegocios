package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.Concepto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Concepto.
 */
@Repository
public interface ConceptoRepository extends JpaRepository<Concepto, Integer> {
    Optional<Concepto> findByNombre(String nombre);

    List<Concepto> findByNegocioId(Integer idNegocio);
}
