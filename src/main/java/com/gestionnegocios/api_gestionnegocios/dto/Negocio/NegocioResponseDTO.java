package com.gestionnegocios.api_gestionnegocios.dto.Negocio;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de negocio.
 * Contiene información básica del negocio.
 */
@Data
public class NegocioResponseDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String telefono;
    private LocalDateTime fechaCreacion;
    private boolean estado;
}
