
package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Login.LoginRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario. Recibe los datos del usuario.
     *
     * @param dto DTO con los datos del usuario a registrar.
     * @return ResponseEntity con el usuario registrado.
     */
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@Validated @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    /**
     * Endpoint para iniciar sesión. Recibe las credenciales del usuario.
     *
     * @param dto DTO con las credenciales de inicio de sesión.
     * @return ResponseEntity con el token JWT o error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    /**
     * Endpoint para desconectar (logout) un usuario. Recibe el token en el header
     * Authorization.
     * 
     * @param authHeader Header de autorización que contiene el token JWT.
     * @return ResponseEntity con mensaje de éxito o error.
     */
    @PreAuthorize("hasAnyRole('ADMIN','CEO','EMPLEADO')")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.logout(authHeader));
    }
}
