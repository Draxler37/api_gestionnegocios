package com.gestionnegocios.api_gestionnegocios.dto.Moneda;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class MonedaRequestDTO {
    @NotBlank
    @Size(max = 50)
    private String nombre;
}
