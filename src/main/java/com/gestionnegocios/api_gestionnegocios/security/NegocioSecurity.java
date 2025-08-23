package com.gestionnegocios.api_gestionnegocios.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.gestionnegocios.api_gestionnegocios.repository.NegocioRepository;

/**
 * Componente de seguridad para verificar la propiedad de un Negocio.
 * Permite verificar si el usuario autenticado es el propietario del Negocio especificado.
 */
@Component("negocioSecurity")
public class NegocioSecurity {
    private final NegocioRepository negocioRepository;

    public NegocioSecurity(NegocioRepository negocioRepository) {
        this.negocioRepository = negocioRepository;
    }

    /**
     * Verifica si el usuario autenticado es el propietario del Negocio especificado.
     *
     * @param auth      Objeto de autenticación que contiene la información del usuario autenticado.
     * @param negocioId ID del Negocio a verificar.
     * @return true si el usuario es el propietario del Negocio, false en caso contrario.
     */
    public boolean isOwner(Authentication auth, Integer negocioId) {
        String email = auth.getName();
        return negocioRepository.findById(negocioId)
                .map(n -> n.getUsuario().getEmail().equals(email))
                .orElse(false);
    }
}
