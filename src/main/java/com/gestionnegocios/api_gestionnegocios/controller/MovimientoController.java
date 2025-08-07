package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {
    private final MovimientoService movimientoService;

    @PreAuthorize("hasRole('CEO')")
    @GetMapping("/cuenta/{idCuenta}")
    public List<MovimientoResponseDTO> getByCuenta(@PathVariable Integer idCuenta) {
        return movimientoService.getByCuentaId(idCuenta);
    }

    @PreAuthorize("hasRole('CEO')")
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(movimientoService.getById(id));
    }

    @PreAuthorize("hasRole('CEO')")
    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> create(@Validated @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.ok(movimientoService.create(dto));
    }

    @PreAuthorize("hasRole('CEO')")
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> update(@PathVariable Integer id,
            @Validated @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.ok(movimientoService.update(id, dto));
    }

    @PreAuthorize("hasRole('CEO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        movimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
