package com.gestionnegocios.api_gestionnegocios.dto.Negocio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para la solicitud de creación o actualización de un negocio.
 * Contiene los campos necesarios para crear o actualizar un negocio.
 */
@Data
public class NegocioRequestDTO {
    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "La descripción del negocio es obligatoria")
    @Size(max = 255)
    private String descripcion;

    @NotBlank(message = "La dirección del negocio es obligatoria")
    @Size(max = 200)
    private String direccion;

    @NotBlank(message = "El teléfono del negocio es obligatorio")
    @Size(max = 20)
    private String telefono;
}