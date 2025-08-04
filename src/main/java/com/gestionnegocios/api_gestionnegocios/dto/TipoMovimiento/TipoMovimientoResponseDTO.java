package com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento;

import lombok.Data;

/**
 * DTO para la respuesta de tipo de movimiento.
 * Contiene información básica del tipo de movimiento.
 */
@Data
public class TipoMovimientoResponseDTO {
    private Integer id;
    private String nombre;
}
