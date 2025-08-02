package com.gestionnegocios.api_gestionnegocios.dto.Rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RolRequestDTO {
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50)
    private String nombre;
}
