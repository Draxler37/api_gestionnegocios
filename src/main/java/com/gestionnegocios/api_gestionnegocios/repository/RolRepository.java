package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Rol.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombre(String nombre);

    List<Rol> findByEstado(boolean estado);
}
