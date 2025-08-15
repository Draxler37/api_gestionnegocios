package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta.TipoCuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta.TipoCuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.TipoCuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-cuenta")
@RequiredArgsConstructor
public class TipoCuentaController {
    private final TipoCuentaService tipoCuentaService;

    @PreAuthorize("hasRole('CEO')")
    @GetMapping
    public List<TipoCuentaResponseDTO> getAll(@RequestParam(required = false) Boolean estado) {
        return tipoCuentaService.getAll(estado);
    }

    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<TipoCuentaResponseDTO> create(@Validated @RequestBody TipoCuentaRequestDTO dto) {
        return ResponseEntity.ok(tipoCuentaService.create(dto));
    }

    @PreAuthorize("hasRole('CEO')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoCuentaResponseDTO> update(@PathVariable Integer id,
            @Validated @RequestBody TipoCuentaRequestDTO dto) {
        return ResponseEntity.ok(tipoCuentaService.update(id, dto));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        boolean ok = tipoCuentaService.desactivar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('CEO')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        boolean ok = tipoCuentaService.activar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('CEO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean ok = tipoCuentaService.delete(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
