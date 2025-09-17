package com.gestionnegocios.api_gestionnegocios.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.gestionnegocios.api_gestionnegocios.repository.CuentaRepository;

/**
 * Componente de seguridad para verificar la propiedad de una Cuenta.
 * Permite verificar si la cuenta pertenece al negocio del CEO autenticado.
 */
@Component("cuentaSecurity")
public class CuentaSecurity {
    private final CuentaRepository cuentaRepository;

    public CuentaSecurity(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Verifica si la cuenta pertenece al negocio del CEO autenticado.
     *
     * @param auth     Objeto de autenticación que contiene la información del
     *                 usuario autenticado.
     * @param cuentaId ID de la cuenta a verificar.
     * @return true si la cuenta pertenece al negocio del CEO autenticado, false en
     *         caso contrario.
     */
    public boolean isOwner(Authentication auth, Integer cuentaId) {
        return cuentaRepository.findById(cuentaId)
                .map(cuenta -> cuenta.getNegocio() != null && cuenta.getNegocio().getUsuario() != null
                        && cuenta.getNegocio().getUsuario().getEmail().equals(auth
                                .getName()))
                .orElse(false);
    }
}
