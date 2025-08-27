package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Moneda.MonedaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.MonedaMapper;
import com.gestionnegocios.api_gestionnegocios.repository.MonedaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonedaService {
    private final MonedaRepository monedaRepository;
    private final MonedaMapper monedaMapper;

    /**
     * Obtiene todas las monedas.
     *
     * @return Lista de MonedaResponseDTO.
     */
    public List<MonedaResponseDTO> getAll() {
        return monedaRepository.findAll().stream()
                .map(monedaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
