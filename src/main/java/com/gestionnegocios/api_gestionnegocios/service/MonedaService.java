package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Moneda.MonedaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Moneda.MonedaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.MonedaMapper;
import com.gestionnegocios.api_gestionnegocios.models.Moneda;
import com.gestionnegocios.api_gestionnegocios.repository.MonedaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonedaService {
    private final MonedaRepository monedaRepository;
    private final MonedaMapper monedaMapper;

    public List<MonedaResponseDTO> getAll(Boolean estado) {
        if (estado == null) {
            return monedaRepository.findAll().stream()
                    .map(monedaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } else if (estado) {
            return monedaRepository.findAll().stream()
                    .filter(Moneda::isEstado)
                    .map(monedaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } else {
            return monedaRepository.findAll().stream()
                    .filter(moneda -> !moneda.isEstado())
                    .map(monedaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public MonedaResponseDTO create(MonedaRequestDTO dto) {
        Moneda moneda = monedaMapper.toEntity(dto);
        return monedaMapper.toResponseDTO(monedaRepository.save(moneda));
    }

    @Transactional
    public MonedaResponseDTO update(Integer id, MonedaRequestDTO dto) {
        Moneda moneda = monedaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moneda no encontrada"));
        monedaMapper.updateEntityFromDto(dto, moneda);
        return monedaMapper.toResponseDTO(monedaRepository.save(moneda));
    }

    @Transactional
    public boolean desactivar(Integer id) {
        return monedaRepository.findById(id).map(moneda -> {
            moneda.setEstado(false);
            monedaRepository.save(moneda);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean activar(Integer id) {
        return monedaRepository.findById(id).map(moneda -> {
            moneda.setEstado(true);
            monedaRepository.save(moneda);
            return true;
        }).orElse(false);
    }

    @Transactional
    public void delete(Integer id) {
        if (!monedaRepository.existsById(id)) {
            throw new RuntimeException("Moneda no encontrada");
        }
        monedaRepository.deleteById(id);
    }
}
