package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Moneda.MonedaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Moneda.MonedaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.MonedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/monedas")
@RequiredArgsConstructor
public class MonedaController {
    private final MonedaService monedaService;

    /**
     * Obtiene todas las Monedas activas e inactivas según el estado.
     *
     * @return Lista de Monedas activas e inactivas según el estado.
     */
    @PreAuthorize("hasRole('CEO')")
    @GetMapping
    public List<MonedaResponseDTO> getAll(@RequestParam(required = false) Boolean estado) {
        return monedaService.getAll(estado);
    }

    /**
     * Crea una nueva Moneda.
     *
     * @param dto MonedaRequestDTO con los datos de la moneda a crear.
     * @return MonedaResponseDTO de la moneda creada.
     */
    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<MonedaResponseDTO> create(@Validated @RequestBody MonedaRequestDTO dto) {
        return ResponseEntity.ok(monedaService.create(dto));
    }

    /**
     * Actualiza una Moneda existente.
     *
     * @param id  ID de la moneda a actualizar.
     * @param dto MonedaRequestDTO con los nuevos datos de la moneda.
     * @return MonedaResponseDTO de la moneda actualizada.
     */
    @PreAuthorize("hasRole('CEO')")
    @PutMapping("/{id}")
    public ResponseEntity<MonedaResponseDTO> update(@PathVariable Integer id,
            @Validated @RequestBody MonedaRequestDTO dto) {
        return ResponseEntity.ok(monedaService.update(id, dto));
    }

    /**
     * Desactiva una Moneda por su ID.
     *
     * @param id ID de la moneda a desactivar.
     * @return Respuesta vacía con estado 204 si se desactivó correctamente, o 404
     *         si no se encontró la moneda.
     */
    @PreAuthorize("hasRole('CEO')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        boolean ok = monedaService.desactivar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Activa una Moneda por su ID.
     *
     * @param id ID de la moneda a activar.
     * @return Respuesta vacía con estado 204 si se activó correctamente, o 404 si
     *         no se encontró la moneda.
     */
    @PreAuthorize("hasRole('CEO')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        boolean ok = monedaService.activar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('CEO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        monedaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
