package com.gestionnegocios.api_gestionnegocios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestionnegocios.api_gestionnegocios.dto.Negocio.NegocioRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Negocio.NegocioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.service.NegocioService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador para manejar las operaciones relacionadas con los Negocios.
 * Permite obtener, crear, actualizar y desactivar Negocios.
 */
@RestController
@RequestMapping("/api/negocios")
@RequiredArgsConstructor
public class NegocioController {
    private final NegocioService negocioService;

    /**
     * Obtiene todos los Negocios activos e inactivos según el estado.
     *
     * @return Lista de Negocios activos e inactivos según el estado.
     */
    @PreAuthorize("hasRole('CEO')")
    @GetMapping
    public List<NegocioResponseDTO> getAll(
            @AuthenticationPrincipal String email,
            @RequestParam(required = false) Boolean estado) {
        return negocioService.getAll(email, estado);
    }

    /**
     * Agrega un nuevo Negocio.
     *
     * @param negocioRequest Datos del Negocio a agregar.
     * @return Negocio creado con estado 201 Created.
     */
    @PreAuthorize("hasRole('CEO')")
    @PostMapping("/add")
    public ResponseEntity<NegocioResponseDTO> addNegocio(@AuthenticationPrincipal String email,
            @Validated @RequestBody NegocioRequestDTO negocioRequest) {
        NegocioResponseDTO createdNegocio = negocioService.addNegocio(email, negocioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNegocio);
    }

    /**
     * Actualiza un Negocio existente.
     *
     * @param id             ID del Negocio a actualizar.
     * @param negocioRequest Datos actualizados del Negocio.
     * @return Negocio actualizado con estado 200 OK o 404 si no existe.
     */
    @PreAuthorize("hasRole('CEO') and @negocioSecurity.isOwner(authentication, #id)")
    @PutMapping("/{id}/update")
    public ResponseEntity<NegocioResponseDTO> updateNegocio(@Validated @PathVariable Integer id,
            @RequestBody NegocioRequestDTO negocioRequest) {
        return negocioService.updateNegocio(id, negocioRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Desactiva un Negocio existente.
     * 
     * @param id ID del Negocio a desactivar.
     * @return Respuesta vacía con estado 204 No Content si se desactivó
     *         correctamente,
     *         o 404 Not Found si el Negocio no existe o ya está inactivo
     */
    @PreAuthorize("hasRole('CEO') and @negocioSecurity.isOwner(authentication, #id)")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateNegocio(@PathVariable Integer id) {
        boolean deactivated = negocioService.deactivateNegocio(id);
        return deactivated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Activa un Negocio existente.
     * 
     * @param id ID del Negocio a activar.
     * @return Respuesta vacía con estado 204 No Content si se activó correctamente,
     *         o 404 Not Found si el Negocio no existe o ya está activo
     */
    @PreAuthorize("hasRole('CEO') and @negocioSecurity.isOwner(authentication, #id)")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateNegocio(@PathVariable Integer id) {
        boolean activated = negocioService.activateNegocio(id);
        return activated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Elimina un negocio por ID.
     * No permite eliminar si el negocio tiene dependencias asociadas.
     *
     * @param id ID del negocio a eliminar.
     * @return Mensaje de éxito o error.
     */
    @PreAuthorize("hasRole('CEO') and @negocioSecurity.isOwner(authentication, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        if (negocioService.delete(id)) {
            return ResponseEntity.ok("Negocio eliminado correctamente");
        }
        return ResponseEntity.badRequest()
                .body("No se puede eliminar: negocio en uso o no existe");
    }

    /**
     * Verifica si un negocio puede ser eliminado.
     *
     * @param id ID del negocio a verificar.
     * @return true si el negocio puede ser eliminado, false si no se encontró o
     *         tiene dependencias.
     */
    @PreAuthorize("hasRole('CEO') and @negocioSecurity.isOwner(authentication, #id)")
    @GetMapping("/{id}/can-delete")
    public ResponseEntity<Boolean> canDelete(@PathVariable Integer id) {
        return ResponseEntity.ok(negocioService.canBeDeleted(id));
    }

    /**
     * Asocia un empleado a un negocio. Solo el CEO propietario puede hacerlo.
     * 
     * @param idNegocio  ID del negocio.
     * @param idEmpleado ID del empleado a asociar.
     * @return Mensaje de éxito.
     */
    @PreAuthorize("hasRole('CEO') and @negocioSecurity.isOwner(authentication, #idNegocio)")
    @PostMapping("/{idNegocio}/empleados/{idEmpleado}")
    public ResponseEntity<String> addEmpleado(@PathVariable Integer idNegocio, @PathVariable Integer idEmpleado) {
        negocioService.addEmpleado(idNegocio, idEmpleado);
        return ResponseEntity.ok("Empleado agregado correctamente");
    }

    /**
     * Lista los empleados asociados a un negocio. Solo el CEO propietario puede
     * verlo.
     * 
     * @param idNegocio ID del negocio.
     * @return Lista de empleados asociados al negocio.
     */
    @PreAuthorize("hasRole('CEO') and @negocioSecurity.isOwner(authentication, #idNegocio)")
    @GetMapping("/{idNegocio}/empleados")
    public ResponseEntity<List<UsuarioResponseDTO>> getEmpleados(
            @PathVariable Integer idNegocio) {
        return ResponseEntity.ok(negocioService.getEmpleados(idNegocio));
    }
}
