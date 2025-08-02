package com.gestionnegocios.api_gestionnegocios.dto.Concepto;

import lombok.Data;

/**
 * DTO para la respuesta de concepto.
 * Contiene información básica del concepto.
 */
@Data
public class ConceptoResponseDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
}
