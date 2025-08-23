package com.gestionnegocios.api_gestionnegocios.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.gestionnegocios.api_gestionnegocios.repository.NegocioRepository;

@Component("negocioSecurity")
public class NegocioSecurity {
    private final NegocioRepository negocioRepository;

    public NegocioSecurity(NegocioRepository negocioRepository) {
        this.negocioRepository = negocioRepository;
    }

    public boolean isOwner(Authentication auth, Integer negocioId) {
        String email = auth.getName();
        return negocioRepository.findById(negocioId)
                .map(n -> n.getUsuario().getEmail().equals(email))
                .orElse(false);
    }
}
