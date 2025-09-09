package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.MovimientoMapper;
import com.gestionnegocios.api_gestionnegocios.models.Movimiento;
import com.gestionnegocios.api_gestionnegocios.repository.MovimientoRepository;
import com.gestionnegocios.api_gestionnegocios.repository.CuentaRepository;
import com.gestionnegocios.api_gestionnegocios.repository.ConceptoRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovimientoService {
        private final MovimientoRepository movimientoRepository;
        private final MovimientoMapper movimientoMapper;
        private final CuentaRepository cuentaRepository;
        private final ConceptoRepository conceptoRepository;

        /**
         * Obtiene movimientos filtrados SOLO del negocio indicado, para CEO o EMPLEADO.
         * Solo se debe enviar el filtro más específico: idCuenta o idConcepto. 
         * El backend resuelve las relaciones y valida pertenencia.
         *
         * @param idCuenta    ID de la cuenta (opcional, si se envía, ignora idNegocio)
         * @param idConcepto  ID del concepto (opcional, si se envía, ignora idNegocio)
         * @param fechaInicio Fecha de inicio (opcional)
         * @param fechaFin    Fecha de fin (opcional)
         * @param montoMaximo Monto máximo (opcional)
         * @return Lista de MovimientoResponseDTO
         */
        public List<MovimientoResponseDTO> getAll(
                        Integer idCuenta,
                        Integer idConcepto,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Double montoMaximo) {
                Integer negocioId = null;
                Integer tipoMovimientoId = null;

                if (idCuenta != null) {
                        var cuenta = cuentaRepository.findById(idCuenta)
                                        .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
                        negocioId = cuenta.getNegocio().getId();
                }

                if (idConcepto != null) {
                        var concepto = conceptoRepository.findById(idConcepto)
                                        .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
                        negocioId = concepto.getNegocio().getId();
                        tipoMovimientoId = concepto.getTipoMovimiento().getId();
                }

                if (negocioId == null) {
                        throw new RuntimeException("Debe especificar idCuenta o idConcepto");
                }

                return movimientoRepository.findByFilters(
                                idCuenta,
                                idConcepto,
                                tipoMovimientoId,
                                negocioId,
                                fechaInicio,
                                fechaFin,
                                montoMaximo).stream()
                                .map(movimientoMapper::toResponseDTO)
                                .collect(Collectors.toList());
        }

        @Transactional
        public MovimientoResponseDTO create(MovimientoRequestDTO dto) {
                Movimiento movimiento = movimientoMapper.toEntity(dto);
                movimiento.setCuenta(cuentaRepository.findById(dto.getIdCuenta())
                                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada")));
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
