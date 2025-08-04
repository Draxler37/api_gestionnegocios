package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento.TipoMovimientoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento.TipoMovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.TipoMovimientoMapper;
import com.gestionnegocios.api_gestionnegocios.models.TipoMovimiento;
import com.gestionnegocios.api_gestionnegocios.repository.TipoMovimientoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoMovimientoService {
    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final TipoMovimientoMapper tipoMovimientoMapper;

    /**
     * Obtiene todos los tipos de movimiento.
     * 
     * @return Lista de TipoMovimientoResponseDTO.
     */
    public List<TipoMovimientoResponseDTO> getAll() {
        return tipoMovimientoRepository.findAll().stream()
                .map(tipoMovimientoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo tipo de movimiento.
     * 
     * @param dto DTO con los datos del tipo de movimiento a crear.
     * @return TipoMovimientoResponseDTO del tipo de movimiento creado.
     */
    @Transactional
    public TipoMovimientoResponseDTO create(TipoMovimientoRequestDTO dto) {
        TipoMovimiento tipoMovimiento = tipoMovimientoMapper.toEntity(dto);
        return tipoMovimientoMapper.toResponseDTO(tipoMovimientoRepository.save(tipoMovimiento));
    }

    /**
     * Actualiza un tipo de movimiento.
     * 
     * @param id ID del tipo de movimiento a actualizar.
     * @param dto DTO con los nuevos datos del tipo de movimiento.
     * @return TipoMovimientoResponseDTO del tipo de movimiento actualizado.
     */
    @Transactional
    public TipoMovimientoResponseDTO update(Integer id, TipoMovimientoRequestDTO dto) {
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TipoMovimiento no encontrado"));
        tipoMovimientoMapper.updateEntityFromDto(dto, tipoMovimiento);
        return tipoMovimientoMapper.toResponseDTO(tipoMovimientoRepository.save(tipoMovimiento));
    }

    /**
     * Elimina un tipo de movimiento.
     * 
     * @param id ID del tipo de movimiento a eliminar.
     */
    @Transactional
    public void delete(Integer id) {
        if (!tipoMovimientoRepository.existsById(id)) {
            throw new RuntimeException("TipoMovimiento no encontrado");
        }
        tipoMovimientoRepository.deleteById(id);
    }
}
