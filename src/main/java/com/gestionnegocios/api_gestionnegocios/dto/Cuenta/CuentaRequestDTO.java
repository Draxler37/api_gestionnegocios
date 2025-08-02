package com.gestionnegocios.api_gestionnegocios.dto.Cuenta;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO para la solicitud de creación o actualización de una cuenta.
 * Contiene los campos necesarios para crear o actualizar una cuenta.
 */
@Data
public class CuentaRequestDTO {
    @NotNull(message = "El ID del negocio es obligatorio")
    private Integer idNegocio;

    @NotNull(message = "El ID del tipo de cuenta es obligatorio")
    private Integer idTipoCuenta;

    @NotNull(message = "El ID de la moneda es obligatorio")
    private Integer idMoneda;

    @NotNull(message = "El balance es obligatorio")
    private BigDecimal balance;

    @NotNull(message = "La descripción es obligatoria")
    @Size(max = 255)
    private String descripcion;
}
