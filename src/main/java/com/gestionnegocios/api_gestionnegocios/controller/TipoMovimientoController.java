package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento.TipoMovimientoRequestDTO;
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

    /**
     * Crea un nuevo tipo de movimiento.
     * 
     * @param dto DTO con los datos del tipo de movimiento a crear.
     * @return TipoMovimientoResponseDTO del tipo de movimiento creado.
     */
    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<TipoMovimientoResponseDTO> create(@RequestBody TipoMovimientoRequestDTO dto) {
        return ResponseEntity.ok(tipoMovimientoService.create(dto));
    }

    /**
     * Actualiza un tipo de movimiento.
     * 
     * @param id ID del tipo de movimiento a actualizar.
     * @param dto DTO con los nuevos datos del tipo de movimiento.
     * @return TipoMovimientoResponseDTO del tipo de movimiento actualizado.
     */
    @PreAuthorize("hasRole('CEO')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoMovimientoResponseDTO> update(@PathVariable Integer id,
            @RequestBody TipoMovimientoRequestDTO dto) {
        return ResponseEntity.ok(tipoMovimientoService.update(id, dto));
    }

    /**
     * Elimina un tipo de movimiento.
     * 
     * @param id ID del tipo de movimiento a eliminar.
     * @return ResponseEntity<Void> indicando el resultado de la operación.
     */
    @PreAuthorize("hasRole('CEO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoMovimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
