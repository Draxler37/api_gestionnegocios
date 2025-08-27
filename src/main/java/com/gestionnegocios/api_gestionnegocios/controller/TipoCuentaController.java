package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta.TipoCuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.TipoCuentaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-cuenta")
@RequiredArgsConstructor
public class TipoCuentaController {
    private final TipoCuentaService tipoCuentaService;

    /**
     * Obtiene todos los tipos de cuenta.
     * 
     * @return Lista de TipoCuentaResponseDTO.
     */
    @PreAuthorize("hasAnyRole('CEO')")
    @GetMapping
    public ResponseEntity<List<TipoCuentaResponseDTO>> getAll() {
        return ResponseEntity.ok(tipoCuentaService.getAll());
    }
}
