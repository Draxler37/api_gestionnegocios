package com.gestionnegocios.api_gestionnegocios.dto.Cuenta;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

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

    @Size(max = 255)
    private String descripcion;

    @NotNull(message = "El número de cuenta es obligatorio")
    @Size(max = 100)
    private String numeroCuenta;
}
