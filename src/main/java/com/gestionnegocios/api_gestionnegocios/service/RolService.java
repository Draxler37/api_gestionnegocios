package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Rol.RolRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Rol.RolResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.RolMapper;
import com.gestionnegocios.api_gestionnegocios.models.Rol;
import com.gestionnegocios.api_gestionnegocios.repository.RolRepository;
import com.gestionnegocios.api_gestionnegocios.repository.UsuarioRolRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para manejar la lógica de negocio relacionada con los roles.
 * Permite obtener, crear, actualizar y eliminar roles.
 */
@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;
    private final RolMapper rolMapper;
    private final UsuarioRolRepository usuarioRolRepository;

    /**
     * Obtiene todos los roles.
     *
     * @return Lista de roles convertidos a DTOs.
     */
    public List<RolResponseDTO> getAll(Boolean estado) {
        if (estado == null) {
            return rolRepository.findAll().stream()
                    .map(rolMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } else if (estado) {
            return rolRepository.findAll().stream()
                    .filter(Rol::isEstado)
                    .map(rolMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } else {
            return rolRepository.findAll().stream()
                    .filter(rol -> !rol.isEstado())
                    .map(rolMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Crea un nuevo rol.
     * 
     * @param dto RolRequestDTO con los datos del rol a crear.
     * @return RolResponseDTO del rol creado.
     */
    @Transactional
    public RolResponseDTO create(RolRequestDTO dto) {
        Rol rol = rolMapper.toEntity(dto);
        Rol saved = rolRepository.save(rol);
        return rolMapper.toResponseDTO(saved);
    }

    /**
     * Actualiza un rol existente.
     *
     * @param id  ID del rol a actualizar.
     * @param dto RolRequestDTO con los nuevos datos del rol.
     * @return RolResponseDTO del rol actualizado, o vacío si no se encontró el rol.
     */
    @Transactional
    public Optional<RolResponseDTO> update(Integer id, RolRequestDTO dto) {
        return rolRepository.findById(id).map(rol -> {
            rolMapper.updateEntityFromDto(dto, rol);
            Rol updated = rolRepository.save(rol);
            return rolMapper.toResponseDTO(updated);
        });
    }

    /**
     * Desactiva un rol por su ID.
     *
     * @param id ID del rol a desactivar.
     * @return true si el rol fue desactivado, false si no se encontró.
     */
    @Transactional
    public boolean desactivar(Integer id) {
        return rolRepository.findById(id).map(rol -> {
            rol.setEstado(false);
            rolRepository.save(rol);
            return true;
        }).orElse(false);
    }

    /**
     * Activa una rol por su ID.
     * 
     * @param id ID de la rol a activar.
     * @return true si la rol fue activada, false si no se encontró.
     */
    @Transactional
    public boolean activar(Integer id) {
        return rolRepository.findById(id).map(rol -> {
            rol.setEstado(true);
            rolRepository.save(rol);
            return true;
        }).orElse(false);
    }

    /**
     * Elimina un rol por ID.
     * No permite eliminar si el rol tiene usuarios asociados.
     * 
     * @param id ID del rol a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró o tiene
     *         usuarios asociados.
     */
    @Transactional
    public boolean delete(Integer id) {
        try {
            rolRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            // No se puede eliminar porque tiene usuarios asociados
            return false;
        }
    }

    /**
     * Verifica si un rol puede ser eliminado.
     *
     * @param id ID del rol a verificar.
     * @return true si el rol puede ser eliminado, false si no se encontró o tiene
     *         usuarios asociados.
     */
    @Transactional
    public boolean canBeDeleted(Integer id) {
        return usuarioRolRepository.countByRolId(id) == 0;
    }
}
