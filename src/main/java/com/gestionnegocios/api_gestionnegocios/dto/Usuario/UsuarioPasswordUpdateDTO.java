package com.gestionnegocios.api_gestionnegocios.dto.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para actualizar la contraseña del usuario.
 */
@Data
public class UsuarioPasswordUpdateDTO {

    @NotBlank(message = "La contraseña antigua no puede estar vacía")
    private String oldPassword;

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 8, max = 255, message = "La nueva contraseña debe tener entre 8 y 255 caracteres")
    private String newPassword;
}
