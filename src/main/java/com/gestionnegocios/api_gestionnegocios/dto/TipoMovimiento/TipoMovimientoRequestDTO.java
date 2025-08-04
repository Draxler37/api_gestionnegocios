package com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para la solicitud de creación o actualización de un tipo de movimiento.
 * Contiene los campos necesarios para crear o actualizar un tipo de movimiento.
 */
@Data
public class TipoMovimientoRequestDTO {
    @NotBlank(message = "El nombre del tipo de movimiento es obligatorio")
    @Size(max = 50)
    private String nombre;
}
