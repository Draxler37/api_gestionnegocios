package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.MovimientoService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {
    private final MovimientoService movimientoService;

    /**
     * Solo CEO dueño o EMPLEADO del negocio pueden ver movimientos de ese negocio.
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
    @PreAuthorize("@movimientoSecurity.isCeoOwnerOrEmpleado(authentication, #idCuenta, #idConcepto)")
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> getAll(
            @RequestParam(required = false) Integer idCuenta,
            @RequestParam(required = true) Integer idConcepto,
            @RequestParam(required = false) LocalDateTime fechaInicio,
            @RequestParam(required = false) LocalDateTime fechaFin,
            @RequestParam(required = false) Double montoMaximo) {
        return ResponseEntity.ok(movimientoService.getAll(
                idCuenta,
                idConcepto,
                fechaInicio,
                fechaFin,
                montoMaximo));
    }

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
}
