package com.gestionnegocios.api_gestionnegocios.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.gestionnegocios.api_gestionnegocios.repository.CuentaRepository;
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

    /**
     * Verifica si el usuario autenticado es CEO dueño del negocio relacionado a la
     * cuenta o concepto.
     */
    public boolean isCeoOwnerByCuenta(Authentication authentication, Integer idCuenta) {
        if (idCuenta == null)
            return false;
        String email = authentication.getName();
        return cuentaRepository.findById(idCuenta)
                .map(cuenta -> cuenta.getNegocio() != null && cuenta.getNegocio().getUsuario() != null
                        && cuenta.getNegocio().getUsuario().getEmail().equals(email))
                .orElse(false);
    }

    public boolean isCeoOwnerByConcepto(Authentication authentication, Integer idConcepto) {
        if (idConcepto == null)
            return false;
        String email = authentication.getName();
        return conceptoRepository.findById(idConcepto)
                .map(concepto -> concepto.getNegocio() != null && concepto.getNegocio().getUsuario() != null
                        && concepto.getNegocio().getUsuario().getEmail().equals(email))
                .orElse(false);
    }

    /**
     * Verifica si el usuario autenticado es EMPLEADO del negocio relacionado a la
     * cuenta o concepto.
     */
    public boolean isEmpleadoByCuenta(Authentication authentication, Integer idCuenta) {
        if (idCuenta == null)
            return false;
        String email = authentication.getName();
        return cuentaRepository.findById(idCuenta)
                .map(cuenta -> cuenta.getNegocio() != null && cuenta.getNegocio().getEmpleados() != null
                        && cuenta.getNegocio().getEmpleados().stream().anyMatch(e -> e.getEmail().equals(email)))
                .orElse(false);
    }

    public boolean isEmpleadoByConcepto(Authentication authentication, Integer idConcepto) {
        if (idConcepto == null)
            return false;
        String email = authentication.getName();
        return conceptoRepository.findById(idConcepto)
                .map(concepto -> concepto.getNegocio() != null && concepto.getNegocio().getEmpleados() != null
                        && concepto.getNegocio().getEmpleados().stream().anyMatch(e -> e.getEmail().equals(email)))
                .orElse(false);
    }

    /**
     * Verifica si el usuario autenticado es CEO dueño o EMPLEADO del negocio
     * relacionado a la cuenta o concepto.
     */
    public boolean isCeoOwnerOrEmpleado(Authentication authentication, Integer idCuenta, Integer idConcepto) {
        // Prioridad: idCuenta > idConcepto
        if (idCuenta != null) {
            return isCeoOwnerByCuenta(authentication, idCuenta) || isEmpleadoByCuenta(authentication, idCuenta);
        } else if (idConcepto != null) {
            return isCeoOwnerByConcepto(authentication, idConcepto) || isEmpleadoByConcepto(authentication, idConcepto);
        }
        return false;
    }
}
