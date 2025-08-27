package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Moneda.MonedaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.MonedaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monedas")
@RequiredArgsConstructor
public class MonedaController {
    private final MonedaService monedaService;

    /**
     * Obtiene todas las monedas.
     *
     * @return Lista de MonedaResponseDTO.
     */
    @PreAuthorize("hasRole('CEO', 'EMPLEADO')")
    @GetMapping
    public ResponseEntity<List<MonedaResponseDTO>> getAll() {
        return ResponseEntity.ok(monedaService.getAll());
    }
}
