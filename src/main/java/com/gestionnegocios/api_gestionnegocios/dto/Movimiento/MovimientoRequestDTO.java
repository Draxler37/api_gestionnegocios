package com.gestionnegocios.api_gestionnegocios.dto.Movimiento;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MovimientoRequestDTO {
    private Integer idCuenta;
    private Integer idTipoMovimiento;
    private Integer idConcepto;
    private BigDecimal monto;
    private String descripcion;
}
