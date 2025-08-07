package com.gestionnegocios.api_gestionnegocios.dto.Cuenta;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de cuenta.
 * Contiene información básica de la cuenta.
 */
@Data
public class CuentaResponseDTO {
    private Integer id;
    private Integer idNegocio;
    private Integer idTipoCuenta;
    private Integer idMoneda;
    private BigDecimal balance;
    private String descripcion;
    private LocalDateTime fechaCreacion;
}
