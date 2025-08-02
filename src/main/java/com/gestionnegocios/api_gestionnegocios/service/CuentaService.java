package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.CuentaMapper;
import com.gestionnegocios.api_gestionnegocios.models.Cuenta;
import com.gestionnegocios.api_gestionnegocios.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    /** 
     * Obtiene las cuentas del CEO autenticado.
     * 
     * @return Lista de cuentas del CEO.
     */
    public List<CuentaResponseDTO> getAll() {
        return cuentaRepository.findAll().stream()
                .map(cuentaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<CuentaResponseDTO> getById(Integer id) {
        return cuentaRepository.findById(id).map(cuentaMapper::toResponseDTO);
    }

    @Transactional
    public CuentaResponseDTO create(CuentaRequestDTO dto) {
        Cuenta cuenta = cuentaMapper.toEntity(dto);
        Cuenta saved = cuentaRepository.save(cuenta);
        return cuentaMapper.toResponseDTO(saved);
    }

    @Transactional
    public Optional<CuentaResponseDTO> update(Integer id, CuentaRequestDTO dto) {
        return cuentaRepository.findById(id).map(cuenta -> {
            cuentaMapper.updateEntityFromDto(dto, cuenta);
            Cuenta updated = cuentaRepository.save(cuenta);
            return cuentaMapper.toResponseDTO(updated);
        });
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!cuentaRepository.existsById(id))
            return false;
        cuentaRepository.deleteById(id);
        return true;
    }
}
