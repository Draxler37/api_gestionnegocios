package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.MovimientoMapper;
import com.gestionnegocios.api_gestionnegocios.models.Movimiento;
import com.gestionnegocios.api_gestionnegocios.repository.MovimientoRepository;
import com.gestionnegocios.api_gestionnegocios.repository.CuentaRepository;
import com.gestionnegocios.api_gestionnegocios.repository.TipoMovimientoRepository;
import com.gestionnegocios.api_gestionnegocios.repository.ConceptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final CuentaRepository cuentaRepository;
    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final ConceptoRepository conceptoRepository;

    public List<MovimientoResponseDTO> getByCuentaId(Integer cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId)
                .stream()
                .map(movimientoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MovimientoResponseDTO getById(Integer id) {
        return movimientoRepository.findById(id)
                .map(movimientoMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }

    @Transactional
    public MovimientoResponseDTO create(MovimientoRequestDTO dto) {
        Movimiento movimiento = movimientoMapper.toEntity(dto);
        movimiento.setCuenta(cuentaRepository.findById(dto.getIdCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada")));
        movimiento.setTipoMovimiento(tipoMovimientoRepository.findById(dto.getIdTipoMovimiento())
                .orElseThrow(() -> new RuntimeException("TipoMovimiento no encontrado")));
        movimiento.setConcepto(conceptoRepository.findById(dto.getIdConcepto())
                .orElseThrow(() -> new RuntimeException("Concepto no encontrado")));
        return movimientoMapper.toResponseDTO(movimientoRepository.save(movimiento));
    }

    @Transactional
    public MovimientoResponseDTO update(Integer id, MovimientoRequestDTO dto) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
        movimientoMapper.updateEntityFromDto(dto, movimiento);
        // No se permite cambiar cuenta, tipoMovimiento ni concepto desde el update
        movimiento.setDescripcion(dto.getDescripcion());
        movimiento.setMonto(dto.getMonto());
        return movimientoMapper.toResponseDTO(movimientoRepository.save(movimiento));
    }
}
