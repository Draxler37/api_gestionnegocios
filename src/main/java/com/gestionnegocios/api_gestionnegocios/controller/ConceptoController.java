package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Concepto.ConceptoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Concepto.ConceptoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.ConceptoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar las operaciones relacionadas con los conceptos.
 * Permite obtener, crear, actualizar y eliminar conceptos.
 */
@RestController
@RequestMapping("/api/conceptos")
@RequiredArgsConstructor
public class ConceptoController {
    private final ConceptoService conceptoService;

    /**
     * Obtiene todos los conceptos.
     *
     * @return Lista de ConceptoResponseDTO.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'CEO', 'EMPLEADO')")
    @GetMapping
    public ResponseEntity<List<ConceptoResponseDTO>> getAll() {
        return ResponseEntity.ok(conceptoService.getAll());
    }

    /**
     * Crea un nuevo concepto.
     * 
     * @param dto ConceptoRequestDTO con los datos del concepto a crear.
     * @return ConceptoResponseDTO del concepto creado.
     */
    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<ConceptoResponseDTO> create(@RequestBody ConceptoRequestDTO dto) {
        return ResponseEntity.ok(conceptoService.create(dto));
    }

    /**
     * Actualiza un concepto existente.
     *
     * @param id  ID del concepto a actualizar.
     * @param dto ConceptoRequestDTO con los nuevos datos del concepto.
     * @return ConceptoResponseDTO del concepto actualizado.
     */
    @PreAuthorize("hasRole('CEO')")
    @PutMapping("/{id}")
    public ResponseEntity<ConceptoResponseDTO> update(@PathVariable Integer id, @RequestBody ConceptoRequestDTO dto) {
        return ResponseEntity.ok(conceptoService.update(id, dto));
    }

    /**
     * Elimina un concepto por su ID.
     *
     * @param id ID del concepto a eliminar.
     * @return Respuesta vacía con código 204 No Content.
     */
    @PreAuthorize("hasRole('CEO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        conceptoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
