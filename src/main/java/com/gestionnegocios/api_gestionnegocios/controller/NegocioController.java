package com.gestionnegocios.api_gestionnegocios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestionnegocios.api_gestionnegocios.dto.Negocio.NegocioRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Negocio.NegocioResponseDTO;
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
    public List<NegocioResponseDTO> getAll(@RequestParam(required = false) Boolean estado) {
        return negocioService.getAll(estado);
    }

    /**
     * Obtiene un Negocio por su ID.
     *
     * @param id ID del Negocio.
     * @return Negocio encontrado o 404 si no existe.
     */
    @PreAuthorize("hasRole('CEO')")
    @GetMapping("/{id}")
    public ResponseEntity<NegocioResponseDTO> getById(@PathVariable Integer id) {
        return negocioService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Agrega un nuevo Negocio.
     *
     * @param negocioRequest Datos del Negocio a agregar.
     * @return Negocio creado con estado 201 Created.
     */
    @PreAuthorize("hasRole('CEO')")
    @PostMapping("/add")
    public ResponseEntity<NegocioResponseDTO> addNegocio(@Validated @RequestBody NegocioRequestDTO negocioRequest) {
        NegocioResponseDTO createdNegocio = negocioService.addNegocio(negocioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNegocio);
    }

    /**
     * Actualiza un Negocio existente.
     *
     * @param id             ID del Negocio a actualizar.
     * @param negocioRequest Datos actualizados del Negocio.
     * @return Negocio actualizado con estado 200 OK o 404 si no existe.
     */
    @PreAuthorize("hasRole('CEO')")
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
    @PreAuthorize("hasRole('CEO')")
    @PostMapping("/{id}/deactivate")
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
    @PreAuthorize("hasRole('CEO')")
    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateNegocio(@PathVariable Integer id) {
        boolean activated = negocioService.activateNegocio(id);
        return activated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
