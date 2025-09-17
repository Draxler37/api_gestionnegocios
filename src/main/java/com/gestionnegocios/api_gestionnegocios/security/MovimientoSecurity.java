package com.gestionnegocios.api_gestionnegocios.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.gestionnegocios.api_gestionnegocios.repository.CuentaRepository;
import com.gestionnegocios.api_gestionnegocios.repository.MovimientoRepository;
import com.gestionnegocios.api_gestionnegocios.repository.ConceptoRepository;

import lombok.RequiredArgsConstructor;

/**
 * Componente de seguridad para verificar la propiedad de un Movimiento.
 * Permite verificar si el usuario autenticado es miembro o propietario del
 * negocio relacionado.
 */
@Component("movimientoSecurity")
@RequiredArgsConstructor
public class MovimientoSecurity {
    private final CuentaRepository cuentaRepository;
    private final ConceptoRepository conceptoRepository;
    private final MovimientoRepository movimientoRepository;

    /**
     * Verifica si el usuario autenticado es CEO dueño del negocio relacionado a la
     * cuenta
     * 
     * @param authentication Objeto de autenticación que contiene la información del
     *                       usuario autenticado.
     * @param idCuenta       ID de la cuenta a verificar.
     * @return true si el usuario es CEO dueño del negocio relacionado a la cuenta,
     *         false en
     *         caso contrario.
     */
    public boolean isCeoOwnerByCuenta(Authentication authentication, Integer idCuenta) {
        if (idCuenta == null) {
            return false;
        }
        String email = authentication.getName();
        return cuentaRepository.findById(idCuenta)
                .map(cuenta -> cuenta.getNegocio() != null && cuenta.getNegocio().getUsuario() != null
                        && cuenta.getNegocio().getUsuario().getEmail().equals(email))
                .orElse(false);
    }

    /**
     * Verifica si el usuario autenticado es CEO dueño del negocio relacionado al
     * concepto.
     * 
     * @param authentication Objeto de autenticación que contiene la información del
     *                       usuario autenticado.
     * @param idConcepto     ID del concepto a verificar.
     * @return true si el usuario es CEO dueño del negocio relacionado al concepto,
     *         false en
     *         caso contrario.
     */
    public boolean isCeoOwnerByConcepto(Authentication authentication, Integer idConcepto) {
        if (idConcepto == null) {
            return false;
        }
        String email = authentication.getName();
        return conceptoRepository.findById(idConcepto)
                .map(concepto -> concepto.getNegocio() != null && concepto.getNegocio().getUsuario() != null
                        && concepto.getNegocio().getUsuario().getEmail().equals(email))
                .orElse(false);
    }

    /**
     * Verifica si el usuario autenticado es CEO dueño del movimiento por
     * idMovimiento.
     * 
     * @param authentication Objeto de autenticación que contiene la información del
     *                       usuario autenticado.
     * @param idMovimiento   ID del movimiento a verificar.
     * @return true si el usuario es CEO dueño del movimiento, false en
     *         caso contrario.
     */
    public boolean isCeoOwnerByMovimiento(Authentication authentication, Integer idMovimiento) {
        if (idMovimiento == null)
            return false;
        String email = authentication.getName();
        // Buscar el movimiento y verificar la cadena de pertenencia
        return cuentaRepository.findById(
                movimientoRepository.findById(idMovimiento)
                        .map(m -> m.getCuenta().getId())
                        .orElse(-1))
                .map(cuenta -> cuenta.getNegocio() != null && cuenta.getNegocio().getUsuario() != null
                        && cuenta.getNegocio().getUsuario().getEmail().equals(email))
                .orElse(false);
    }
}
