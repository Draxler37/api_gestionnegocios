package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioPasswordUpdateDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioRolRequestDTO;
import com.gestionnegocios.api_gestionnegocios.service.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    /**
     * Obtiene todos los usuarios activos e inactivos según el estado.
     *
     * @param estado Estado del usuario (null, true o false). Si es null devuelve
     *               todos los usuarios,
     *               si es true devuelve solo los activos, si es false devuelve solo
     *               los inactivos.
     * @return Lista de usuarios activos e inactivos según el estado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UsuarioResponseDTO> getAll(@RequestParam(required = false) Boolean estado) {
        return usuarioService.getAll(estado);
    }

    /**
     * Obtiene el usuario autenticado.
     *
     * @return Usuario encontrado o 404 si no existe.
     */
    @PreAuthorize("hasAnyRole('ADMIN','CEO','EMPLEADO')")
    @GetMapping("/my")
    public ResponseEntity<UsuarioResponseDTO> getAuthenticatedUser() {
        return usuarioService.getAuthenticatedUser()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza la contraseña del usuario.
     *
     * @param id  ID del usuario a actualizar.
     * @param dto DTO con la contraseña antigua y la nueva.
     * @return Usuario actualizado o 404 si no existe.
     */
    @PreAuthorize("hasAnyRole('ADMIN','CEO','EMPLEADO')")
    @PutMapping("/{id}/update_password")
    public ResponseEntity<UsuarioResponseDTO> updatePassword(@Validated @RequestBody UsuarioPasswordUpdateDTO dto) {
        return usuarioService.updatePassword(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Desactiva un usuario por id (acción de administrador).
     * 
     * @param id ID del usuario a desactivar.
     * @return 204 No Content si se desactivó correctamente, 404 si no existe.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUsuario(@PathVariable Integer id) {
        boolean deleted = usuarioService.deactivateUsuario(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Activa un usuario por id (acción de administrador).
     * 
     * @param id ID del usuario a activar.
     * @return 200 OK si se activó correctamente, 404 si no existe.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UsuarioResponseDTO> activarUsuario(@PathVariable Integer id) {
        return usuarioService.activarUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Asigna un rol a un usuario.
     *
     * @param dto DTO con los IDs del usuario y del rol.
     * @return Mensaje de éxito o error.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles")
    public ResponseEntity<String> addRolToUsuario(@RequestBody UsuarioRolRequestDTO dto) {
        boolean added = usuarioService.addRolToUsuario(dto.getIdUsuario(), dto.getIdRol());
        if (added) {
            return ResponseEntity.ok("Rol añadido correctamente");
        }
        return ResponseEntity.badRequest().body("No se pudo añadir el rol");
    }

    /**
     * Elimina un rol de un usuario.
     *
     * @param dto DTO con los IDs del usuario y del rol.
     * @return Mensaje de éxito o error.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/roles")
    public ResponseEntity<String> removeRolFromUsuario(@RequestBody UsuarioRolRequestDTO dto) {
        boolean removed = usuarioService.removeRolFromUsuario(dto.getIdUsuario(), dto.getIdRol());
        if (removed) {
            return ResponseEntity.ok("Rol eliminado correctamente");
        }
        return ResponseEntity.badRequest().body("No se pudo eliminar el rol");
    }
}
