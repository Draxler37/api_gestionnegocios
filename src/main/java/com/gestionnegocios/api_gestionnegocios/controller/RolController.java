package com.gestionnegocios.api_gestionnegocios.controller;

import com.gestionnegocios.api_gestionnegocios.dto.Rol.RolRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Rol.RolResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.RolService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar las operaciones relacionadas con los roles.
 * Permite obtener, crear, actualizar y eliminar roles.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {
    private final RolService rolService;

    /**
     * Obtiene todos los roles.
     *
     * @return Lista de roles.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<RolResponseDTO> getAll() {
        return rolService.getAll();
    }

    /**
     * Crea un nuevo rol.
     * 
     * @param dto RolRequestDTO con los datos del rol a crear.
     * @return RolResponseDTO del rol creado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RolResponseDTO> create(@Validated @RequestBody RolRequestDTO dto) {
        return ResponseEntity.ok(rolService.create(dto));
    }

    /**
     * Actualiza un rol existente.
     *
     * @param id  ID del rol a actualizar.
     * @param dto RolRequestDTO con los nuevos datos del rol.
     * @return RolResponseDTO del rol actualizado, o 404 si no se encontró el rol.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RolResponseDTO> update(@PathVariable Integer id, @Validated @RequestBody RolRequestDTO dto) {
        return rolService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un rol por ID.
     * No permite eliminar si el rol tiene usuarios asociados.
     *
     * @param id ID del rol a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró o tiene
     *         usuarios asociados.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        boolean deleted = rolService.delete(id);
        if (deleted)
            return ResponseEntity.ok("Rol eliminado correctamente");
        return ResponseEntity.badRequest()
                .body("No se puede eliminar el rol porque tiene usuarios asociados o no existe");
    }
}
