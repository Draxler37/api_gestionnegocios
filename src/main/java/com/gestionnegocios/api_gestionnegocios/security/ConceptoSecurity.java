package com.gestionnegocios.api_gestionnegocios.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.gestionnegocios.api_gestionnegocios.repository.ConceptoRepository;

import lombok.RequiredArgsConstructor;

/**
 * Componente de seguridad para verificar la propiedad de un Concepto.
 * Permite verificar si el usuario autenticado es el propietario del Negocio
 * asociado al Concepto especificado.
 */
@Component("conceptoSecurity")
@RequiredArgsConstructor
public class ConceptoSecurity {
    /**
     * Verifica si el usuario autenticado es empleado del Negocio asociado al
     * Concepto especificado.
     * 
     * @param authentication Objeto de autenticación que contiene la información del
     *                       usuario autenticado.
     * @param idNegocio      ID del Negocio a verificar.
     * @return true si el usuario es empleado del Negocio, false en caso contrario.
     */
    public boolean isMember(Authentication authentication, Integer idNegocio) {
        String email = authentication.getName();
        return conceptoRepository.findByNegocioId(idNegocio).stream()
                .findFirst()
                .map(concepto -> {
                    if (concepto.getNegocio().getEmpleados() != null) {
                        return concepto.getNegocio().getEmpleados().stream()
                                .anyMatch(e -> e.getEmail().equals(email));
                    }
                    return false;
                }).orElse(false);
    }

    private final ConceptoRepository conceptoRepository;

    /**
     * Verifica si el usuario autenticado es el propietario del Negocio asociado al
     * Concepto especificado.
     *
     * @param authentication Objeto de autenticación que contiene la información del
     *                       usuario autenticado.
     * @param conceptoId     ID del Concepto a verificar.
     * @return true si el usuario es el propietario del Negocio asociado al
     *         Concepto, false en caso contrario.
     */
    public boolean isOwner(Authentication authentication, Integer conceptoId) {
        String email = authentication.getName();
        return conceptoRepository.findById(conceptoId)
                .map(concepto -> concepto.getNegocio().getUsuario().getEmail().equals(email))
                .orElse(false);
    }
}
