package com.gestionnegocios.api_gestionnegocios.dto.Usuario;

import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para la solicitud de asignación de un rol a un usuario.
 * Contiene los identificadores del usuario y del rol.
 */
@Data
public class UsuarioRolRequestDTO {
    @NotBlank(message = "El ID del usuario no puede estar vacío")
    @NegativeOrZero(message = "El ID del usuario debe ser un número entero positivo")
    private Integer idUsuario;

    @NotBlank(message = "El ID del rol no puede estar vacío")
    @NegativeOrZero(message = "El ID del rol debe ser un número entero positivo")
    private Integer idRol;
}
