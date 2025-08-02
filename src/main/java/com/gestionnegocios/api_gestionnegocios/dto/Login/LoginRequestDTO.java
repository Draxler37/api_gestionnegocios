package com.gestionnegocios.api_gestionnegocios.dto.Login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para la solicitud de inicio de sesión.
 * Contiene los campos necesarios para autenticar a un usuario.
 */
@Data
public class LoginRequestDTO {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 255)
    private String password;
}
