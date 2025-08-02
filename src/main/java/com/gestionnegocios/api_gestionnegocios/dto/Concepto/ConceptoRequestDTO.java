package com.gestionnegocios.api_gestionnegocios.dto.Concepto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para la solicitud de creación o actualización de un concepto.
 * Contiene los campos necesarios para crear o actualizar un concepto.
 */
@Data
public class ConceptoRequestDTO {
    @NotNull(message = "El ID del negocio es obligatorio")
    private Integer idNegocio;

    @NotNull(message = "El ID del tipo de movimiento es obligatorio")
    private Integer idTipoMovimiento;

    @NotBlank(message = "El nombre del concepto es obligatorio")
    @Size(max = 50)
    private String nombre;

    @NotBlank(message = "La descripción del concepto es obligatoria")
    @Size(max = 255)
    private String descripcion;
}
