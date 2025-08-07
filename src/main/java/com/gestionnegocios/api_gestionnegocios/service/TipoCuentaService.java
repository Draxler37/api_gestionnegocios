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

    public List<TipoCuentaResponseDTO> getAll() {
        return tipoCuentaRepository.findAll().stream()
                .map(tipoCuentaMapper::toResponseDTO)
                .collect(Collectors.toList());
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
    public void delete(Integer id) {
        if (!tipoCuentaRepository.existsById(id)) {
            throw new RuntimeException("TipoCuenta no encontrado");
        }
        tipoCuentaRepository.deleteById(id);
    }
}
