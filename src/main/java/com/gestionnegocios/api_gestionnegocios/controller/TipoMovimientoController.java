package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento.TipoMovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.TipoMovimientoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar las operaciones relacionadas con los tipos de movimiento.
 * Permite obtener, crear, actualizar y eliminar tipos de movimiento.
 */
@RestController
@RequestMapping("/api/tipos-movimiento")
@RequiredArgsConstructor
public class TipoMovimientoController {
    private final TipoMovimientoService tipoMovimientoService;

    /**
     * Obtiene todos los tipos de movimiento.
     * 
     * @return Lista de TipoMovimientoResponseDTO.
     */
    @PreAuthorize("hasAnyRole('CEO', 'EMPLEADO')")
    @GetMapping
    public ResponseEntity<List<TipoMovimientoResponseDTO>> getAll() {
        return ResponseEntity.ok(tipoMovimientoService.getAll());
    }
}
