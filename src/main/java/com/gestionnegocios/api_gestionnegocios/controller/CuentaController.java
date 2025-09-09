package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.CuentaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar las operaciones relacionadas con las cuentas.
 * Permite obtener, crear, actualizar, activar y desactivar cuentas.
 */
@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService cuentaService;

    /**
     * Obtiene todas las cuentas de un negocio específico.
     * Solo el CEO propietario del negocio puede verlas.
     * 
     * @param id     ID del negocio para filtrar las cuentas.
     * @param estado (Opcional) Filtra por estado (activo/inactivo).
     * @return Lista de CuentaResponseDTO.
     */
    @PreAuthorize("(hasRole('CEO') and @negocioSecurity.isOwner(authentication, #id))")
    @GetMapping("/{id}/negocio")
    public List<CuentaResponseDTO> getAllByNegocio(@PathVariable Integer id,
            @RequestParam(required = false) Boolean estado) {
        return cuentaService.getAllByNegocio(id, estado);
    }

    /**
     * Crea una nueva cuenta.
     * 
     * @param dto CuentaRequestDTO con los datos de la cuenta a crear.
     * @return CuentaResponseDTO de la cuenta creada.
     */
    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<CuentaResponseDTO> create(@Validated @RequestBody CuentaRequestDTO dto) {
        return ResponseEntity.ok(cuentaService.create(dto));
    }

    /**
     * Actualiza la descripción de una cuenta.
     * Solo el CEO propietario puede hacerlo.
     * 
     * @param id          ID de la cuenta a actualizar.
     * @param descripcion Nueva descripción para la cuenta.
     * @return CuentaResponseDTO de la cuenta actualizada.
     */
    @PreAuthorize("hasRole('CEO') and @cuentaSecurity.isOwner(authentication, #id)")
    @PatchMapping("/{id}/descripcion")
    public ResponseEntity<CuentaResponseDTO> updateDescripcion(@PathVariable Integer id,
            @RequestBody String descripcion) {
        return cuentaService.updateDescripcion(id, descripcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Desactiva una cuenta.
     * Solo el CEO propietario puede hacerlo.
     * 
     * @param id ID de la cuenta a desactivar.
     * @return 204 No Content si se desactiva, 404 Not Found si
     */
    @PreAuthorize("hasRole('CEO') and @cuentaSecurity.isOwner(authentication, #id)")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> desactivar(@PathVariable Integer id) {
        boolean ok = cuentaService.desactivar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Activa una cuenta.
     * Solo el CEO propietario puede hacerlo.
     * 
     * @param id ID de la cuenta a activar.
     * @return 204 No Content si se activa, 404 Not Found si
     */
    @PreAuthorize("hasRole('CEO') and @cuentaSecurity.isOwner(authentication, #id)")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activar(@PathVariable Integer id) {
        boolean ok = cuentaService.activar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Obtiene los movimientos asociados a una cuenta.
     * Solo el CEO propietario puede verlo.
     * 
     * @param id ID de la cuenta.
     * @return Lista de MovimientoResponseDTO asociados a la cuenta.
     */
    @PreAuthorize("hasRole('CEO') and @cuentaSecurity.isOwner(authentication, #id)")
    @GetMapping("/{id}/movimientos")
    public ResponseEntity<List<MovimientoResponseDTO>> getMovimientos(
            @PathVariable Integer id) {
        return ResponseEntity.ok(cuentaService.getMovimientos(id));
    }
}
