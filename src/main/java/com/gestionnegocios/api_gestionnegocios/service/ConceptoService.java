package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Concepto.ConceptoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Concepto.ConceptoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.ConceptoMapper;
import com.gestionnegocios.api_gestionnegocios.models.Concepto;
import com.gestionnegocios.api_gestionnegocios.models.Negocio;
import com.gestionnegocios.api_gestionnegocios.models.TipoMovimiento;
import com.gestionnegocios.api_gestionnegocios.repository.ConceptoRepository;
import com.gestionnegocios.api_gestionnegocios.repository.NegocioRepository;
import com.gestionnegocios.api_gestionnegocios.repository.TipoMovimientoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejar la lógica de negocio relacionada con los conceptos.
 * Permite obtener, crear, actualizar y eliminar conceptos.
 */
@Service
@RequiredArgsConstructor
public class ConceptoService {
    private final com.gestionnegocios.api_gestionnegocios.repository.MovimientoRepository movimientoRepository;
    private final ConceptoRepository conceptoRepository;
    private final NegocioRepository negocioRepository;
    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final ConceptoMapper conceptoMapper;

    /**
     * Obtiene todos los conceptos de un negocio específico.
     *
     * @param idNegocio ID del negocio para filtrar los conceptos.
     * @return Lista de ConceptoResponseDTO.
     */
    public List<ConceptoResponseDTO> getAll(Integer idNegocio) {
        return conceptoRepository.findByNegocioId(idNegocio).stream()
                .map(conceptoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo concepto.
     *
     * @param dto ConceptoRequestDTO con los datos del concepto a crear.
     * @return ConceptoResponseDTO del concepto creado.
     */
    @Transactional
    public ConceptoResponseDTO create(ConceptoRequestDTO dto) {
        Negocio negocio = negocioRepository.findById(dto.getIdNegocio())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(dto.getIdTipoMovimiento())
                .orElseThrow(() -> new RuntimeException("Tipo de Movimiento no encontrado"));

        Concepto concepto = conceptoMapper.toEntity(dto);
        concepto.setNegocio(negocio);
        concepto.setTipoMovimiento(tipoMovimiento);
        return conceptoMapper.toResponseDTO(conceptoRepository.save(concepto));
    }

    /**
     * Actualiza un concepto existente.
     *
     * @param id  ID del concepto a actualizar.
     * @param dto ConceptoRequestDTO con los nuevos datos del concepto.
     * @return ConceptoResponseDTO del concepto actualizado.
     */
    @Transactional
    public ConceptoResponseDTO update(Integer id, ConceptoRequestDTO dto) {
        Concepto concepto = conceptoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));

        if (dto.getIdNegocio() != null) {
            Negocio negocio = negocioRepository.findById(dto.getIdNegocio())
                    .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
            concepto.setNegocio(negocio);
        }

        if (dto.getIdTipoMovimiento() != null) {
            TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(dto.getIdTipoMovimiento())
                    .orElseThrow(() -> new RuntimeException("TipoMovimiento no encontrado"));
            concepto.setTipoMovimiento(tipoMovimiento);
        }

        conceptoMapper.updateEntityFromDto(dto, concepto);
        return conceptoMapper.toResponseDTO(conceptoRepository.save(concepto));
    }

    /**
     * Elimina un concepto por su ID.
     *
     * @param id ID del concepto a eliminar.
     * @throws RuntimeException si el concepto no existe.
     */
    @Transactional
    public void delete(Integer id) {
        if (!conceptoRepository.existsById(id)) {
            throw new RuntimeException("Concepto no encontrado");
        }
        // Verificar si el concepto está usado en algún movimiento
        if (movimientoRepository.existsByConceptoId(id)) {
            throw new RuntimeException("No se puede eliminar: el concepto está usado en al menos un movimiento.");
        }
        conceptoRepository.deleteById(id);
    }
}
