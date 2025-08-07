package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.CuentaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService cuentaService;

    @PreAuthorize("hasRole('CEO')")
    @GetMapping
    public List<CuentaResponseDTO> getAllByNegocio(@RequestParam Integer idNegocio,
            @RequestParam(required = false) Boolean estado) {
        return cuentaService.getAllByNegocio(idNegocio, estado);
    }

    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<CuentaResponseDTO> create(@Validated @RequestBody CuentaRequestDTO dto) {
        return ResponseEntity.ok(cuentaService.create(dto));
    }

    /**
     * Solo permite actualizar la descripción de la cuenta.
     */
    @PreAuthorize("hasRole('CEO')")
    @PatchMapping("/{id}/descripcion")
    public ResponseEntity<CuentaResponseDTO> updateDescripcion(@PathVariable Integer id,
            @RequestBody String descripcion) {
        return cuentaService.updateDescripcion(id, descripcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Desactiva una cuenta (soft delete).
     */
    @PreAuthorize("hasRole('CEO')")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Integer id) {
        boolean ok = cuentaService.desactivar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Activa una cuenta.
     */
    @PreAuthorize("hasRole('CEO')")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Integer id) {
        boolean ok = cuentaService.activar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Lista los movimientos de una cuenta.
     */
    @PreAuthorize("hasRole('CEO')")
    @GetMapping("/{id}/movimientos")
    public ResponseEntity<List<com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO>> getMovimientos(
            @PathVariable Integer id) {
        return ResponseEntity.ok(cuentaService.getMovimientos(id));
    }
}
