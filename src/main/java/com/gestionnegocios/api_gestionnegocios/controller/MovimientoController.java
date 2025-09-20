package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.MovimientoService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {
    private final MovimientoService movimientoService;

    /**
     * Solo CEO dueño del negocio pueden ver movimientos de ese negocio.
     * Se debe enviar solo uno de los siguientes filtros (en orden de prioridad):
     * - idCuenta: filtra movimientos de una cuenta específica (y resuelve el
     * negocio automáticamente)
     * - idConcepto: filtra movimientos de un concepto específico (y resuelve el
     * negocio y tipoMovimiento automáticamente)
     *
     * @param idCuenta    ID de la cuenta para filtrar los movimientos (opcional)
     * @param idConcepto  ID del concepto para filtrar los movimientos (opcional)
     * @param fechaInicio Fecha de inicio para filtrar los movimientos (opcional)
     * @param fechaFin    Fecha de fin para filtrar los movimientos (opcional)
     * @param montoMaximo Monto máximo para filtrar los movimientos (opcional)
     * @return Lista de MovimientoResponseDTO
     */
    @PreAuthorize("hasRole('CEO') and (@movimientoSecurity.isCeoOwnerByCuenta(authentication, #idCuenta) or @movimientoSecurity.isCeoOwnerByConcepto(authentication, #idConcepto))")
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> getAll(
            @RequestParam(name = "idCuenta", required = true) Integer idCuenta,
            @RequestParam(name = "idConcepto", required = true) Integer idConcepto,
            @RequestParam(name = "fechaInicio", required = false) LocalDateTime fechaInicio,
            @RequestParam(name = "fechaFin", required = false) LocalDateTime fechaFin,
            @RequestParam(name = "montoMaximo", required = false) BigDecimal montoMaximo) {
        return ResponseEntity.ok(movimientoService.getAll(
                idCuenta,
                idConcepto,
                fechaInicio,
                fechaFin,
                montoMaximo));
    }

    @PreAuthorize("hasRole('CEO')")
    @PostMapping("/add")
    public ResponseEntity<MovimientoResponseDTO> create(@AuthenticationPrincipal String email,
            @Validated @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.ok(movimientoService.create(email, dto));
    }

    @PreAuthorize("hasRole('CEO') and @movimientoSecurity.isCeoOwnerByMovimiento(authentication, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> update(@PathVariable Integer id,
            @Validated @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.ok(movimientoService.update(id, dto));
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
        return ResponseEntity.ok(movimientoService.getMovimientos(id));
    }
}
