package com.gestionnegocios.api_gestionnegocios.dto.Usuario;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de usuario (sin password).
 */
@Data
public class UsuarioResponseDTO {
    private Integer id;
    private String nombre;
    private String email;
    private String telefono;
    private LocalDateTime fechaRegistro;
    private boolean estado;
}
