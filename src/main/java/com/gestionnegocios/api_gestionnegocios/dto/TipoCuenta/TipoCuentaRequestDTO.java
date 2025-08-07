package com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class TipoCuentaRequestDTO {
    @NotBlank
    @Size(max = 50)
    private String nombre;
}
