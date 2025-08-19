package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento.TipoMovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.TipoMovimientoMapper;
import com.gestionnegocios.api_gestionnegocios.repository.TipoMovimientoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

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
}
