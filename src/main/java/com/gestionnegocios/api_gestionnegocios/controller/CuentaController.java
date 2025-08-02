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
    public List<CuentaResponseDTO> getAll() {
        return cuentaService.getAll();
    }

    @PreAuthorize("hasRole('CEO')")
    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> getById(@PathVariable Integer id) {
        return cuentaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<CuentaResponseDTO> create(@Validated @RequestBody CuentaRequestDTO dto) {
        return ResponseEntity.ok(cuentaService.create(dto));
    }

    @PreAuthorize("hasRole('CEO')")
    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> update(@PathVariable Integer id,
            @Validated @RequestBody CuentaRequestDTO dto) {
        return cuentaService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('CEO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = cuentaService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
