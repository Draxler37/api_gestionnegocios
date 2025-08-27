package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta.TipoCuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.TipoCuentaMapper;
import com.gestionnegocios.api_gestionnegocios.repository.TipoCuentaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoCuentaService {
    private final TipoCuentaRepository tipoCuentaRepository;
    private final TipoCuentaMapper tipoCuentaMapper;

    /**
     * Obtiene todos los tipos de cuenta.
     * 
     * @return Lista de TipoCuentaResponseDTO.
     */
    public List<TipoCuentaResponseDTO> getAll() {
        return tipoCuentaRepository.findAll().stream()
                .map(tipoCuentaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
