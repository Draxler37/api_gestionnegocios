package com.gestionnegocios.api_gestionnegocios.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para la creación y actualización de usuarios (entrada).
 */
@Data
public class UsuarioRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Email(message = "El formato del email es inválido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
    private String password;

    @Size(max = 20)
    private String telefono;
}
