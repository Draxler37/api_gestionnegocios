package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Negocio.
 */
@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Integer> {
    List<Negocio> findByUsuarioId(Integer usuarioId);

    List<Negocio> findByUsuarioEmail(String email);

    List<Negocio> findByUsuarioEmailAndEstado(String email, boolean estado);
}
