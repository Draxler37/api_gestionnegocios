package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Rol.RolRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Rol.RolResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.RolMapper;
import com.gestionnegocios.api_gestionnegocios.models.Rol;
import com.gestionnegocios.api_gestionnegocios.repository.RolRepository;

import lombok.RequiredArgsConstructor;

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

    /**
     * Obtiene todos los roles.
     *
     * @return Lista de roles convertidos a DTOs.
     */
    public List<RolResponseDTO> getAll() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toResponseDTO)
                .collect(Collectors.toList());
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
     * Elimina un rol por ID.
     * No permite eliminar si el rol tiene usuarios asociados.
     *
     * @param id ID del rol a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró o tiene
     *         usuarios asociados.
     */
    @Transactional
    public boolean delete(Integer id) {
        Optional<Rol> rolOpt = rolRepository.findById(id);
        if (rolOpt.isEmpty())
            return false;
        Rol rol = rolOpt.get();
        // No permitir eliminar si tiene usuarios asociados
        if (rol.getUsuarios() != null && !rol.getUsuarios().isEmpty()) {
            return false;
        }
        rolRepository.delete(rol);
        return true;
    }
}
