package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta.TipoCuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta.TipoCuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.TipoCuentaMapper;
import com.gestionnegocios.api_gestionnegocios.models.TipoCuenta;
import com.gestionnegocios.api_gestionnegocios.repository.TipoCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoCuentaService {
    private final TipoCuentaRepository tipoCuentaRepository;
    private final TipoCuentaMapper tipoCuentaMapper;

    public List<TipoCuentaResponseDTO> getAll(Boolean estado) {
        if (estado == null) {
            return tipoCuentaRepository.findAll().stream()
                    .map(tipoCuentaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } else if (estado) {
            return tipoCuentaRepository.findAll().stream()
                    .filter(TipoCuenta::isEstado)
                    .map(tipoCuentaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } else {
            return tipoCuentaRepository.findAll().stream()
                    .filter(tipocuenta -> !tipocuenta.isEstado())
                    .map(tipoCuentaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public TipoCuentaResponseDTO create(TipoCuentaRequestDTO dto) {
        TipoCuenta tipoCuenta = tipoCuentaMapper.toEntity(dto);
        return tipoCuentaMapper.toResponseDTO(tipoCuentaRepository.save(tipoCuenta));
    }

    @Transactional
    public TipoCuentaResponseDTO update(Integer id, TipoCuentaRequestDTO dto) {
        TipoCuenta tipoCuenta = tipoCuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TipoCuenta no encontrado"));
        tipoCuentaMapper.updateEntityFromDto(dto, tipoCuenta);
        return tipoCuentaMapper.toResponseDTO(tipoCuentaRepository.save(tipoCuenta));
    }

    @Transactional
    public boolean desactivar(Integer id) {
        return tipoCuentaRepository.findById(id).map(tipoCuenta -> {
            tipoCuenta.setEstado(false);
            tipoCuentaRepository.save(tipoCuenta);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean activar(Integer id) {
        return tipoCuentaRepository.findById(id).map(tipoCuenta -> {
            tipoCuenta.setEstado(true);
            tipoCuentaRepository.save(tipoCuenta);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean delete(Integer id) {
        return tipoCuentaRepository.findById(id).map(tipoCuenta -> {
            tipoCuentaRepository.delete(tipoCuenta);
            return true;
        }).orElse(false);
    }
}
