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

    @PreAuthorize("hasRole('CEO')")
    @GetMapping
    public List<MonedaResponseDTO> getAll() {
        return monedaService.getAll();
    }

    @PreAuthorize("hasRole('CEO')")
    @GetMapping("/{id}")
    public ResponseEntity<MonedaResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(monedaService.getById(id));
    }

    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<MonedaResponseDTO> create(@Validated @RequestBody MonedaRequestDTO dto) {
        return ResponseEntity.ok(monedaService.create(dto));
    }

    @PreAuthorize("hasRole('CEO')")
    @PutMapping("/{id}")
    public ResponseEntity<MonedaResponseDTO> update(@PathVariable Integer id,
            @Validated @RequestBody MonedaRequestDTO dto) {
        return ResponseEntity.ok(monedaService.update(id, dto));
    }

    @PreAuthorize("hasRole('CEO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        monedaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
