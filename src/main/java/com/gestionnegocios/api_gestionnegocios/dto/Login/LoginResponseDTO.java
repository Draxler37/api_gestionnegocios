package com.gestionnegocios.api_gestionnegocios.dto.Login;

import java.util.List;

import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de inicio de sesión.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private List<String> roles;
    private UsuarioResponseDTO usuario;
}
