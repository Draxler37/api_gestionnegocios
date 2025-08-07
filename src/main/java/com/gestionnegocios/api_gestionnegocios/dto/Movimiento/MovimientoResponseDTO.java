package com.gestionnegocios.api_gestionnegocios.dto.Movimiento;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimientoResponseDTO {
    private Integer id;
    private Integer idCuenta;
    private Integer idTipoMovimiento;
    private Integer idConcepto;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fechaMovimiento;
}
