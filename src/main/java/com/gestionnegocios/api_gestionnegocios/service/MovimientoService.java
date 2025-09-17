package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.MovimientoMapper;
import com.gestionnegocios.api_gestionnegocios.models.Concepto;
import com.gestionnegocios.api_gestionnegocios.models.Cuenta;
import com.gestionnegocios.api_gestionnegocios.models.Movimiento;
import com.gestionnegocios.api_gestionnegocios.models.TipoMovimiento;
import com.gestionnegocios.api_gestionnegocios.repository.MovimientoRepository;
import com.gestionnegocios.api_gestionnegocios.repository.CuentaRepository;
import com.gestionnegocios.api_gestionnegocios.repository.ConceptoRepository;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
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
         * Obtiene una lista de movimientos filtrados por los parámetros proporcionados.
         * 
         * @param idCuenta    ID de la cuenta para filtrar los movimientos
         * @param idConcepto  ID del concepto para filtrar los movimientos
         * @param fechaInicio Fecha de inicio para filtrar los movimientos (opcional)
         * @param fechaFin    Fecha de fin para filtrar los movimientos (opcional)
         * @param montoMaximo Monto máximo para filtrar los movimientos (opcional)
         * @return Lista de MovimientoResponseDTO
         */
        @Transactional(readOnly = true)
        public List<MovimientoResponseDTO> getAll(
                        Integer idCuenta,
                        Integer idConcepto,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        BigDecimal montoMaximo) {

                if (!cuentaRepository.existsById(idCuenta)) {
                        throw new RuntimeException("Cuenta no encontrada");
                }
                if (!conceptoRepository.existsById(idConcepto)) {
                        throw new RuntimeException("Concepto no encontrada");
                }

                return movimientoRepository.findByFilters(
                                idCuenta,
                                idConcepto,
                                fechaInicio,
                                fechaFin,
                                montoMaximo).stream()
                                .map(movimientoMapper::toResponseDTO)
                                .collect(Collectors.toList());
        }

        @Transactional
        public MovimientoResponseDTO create(String email, MovimientoRequestDTO dto) {
                Cuenta cuenta = cuentaRepository.findById(dto.getIdCuenta())
                                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

                if(!cuenta.getNegocio().getUsuario().getEmail().equals(email)){
                        throw new RuntimeException("No tienes permisos para crear movimientos en esta cuenta");
                }

                // Obtener el concepto y el tipo de movimiento
                Concepto concepto = conceptoRepository.findById(dto.getIdConcepto())
                                .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));

                TipoMovimiento tipoMovimiento = concepto.getTipoMovimiento();
                if (tipoMovimiento == null || tipoMovimiento.getNombre() == null) {
                        throw new RuntimeException("Tipo de movimiento no definido en el concepto");
                }

                // Actualizar el balance de la cuenta según el tipo de movimiento
                BigDecimal monto = dto.getMonto();
                String tipo = tipoMovimiento.getNombre().toUpperCase();
                if ("DEBITO".equals(tipo)) {
                        cuenta.setBalance(cuenta.getBalance().subtract(monto));
                } else if ("CREDITO".equals(tipo)) {
                        cuenta.setBalance(cuenta.getBalance().add(monto));
                } else {
                        throw new RuntimeException("Tipo de movimiento no soportado: " + tipo);
                }
                cuentaRepository.save(cuenta);

                Movimiento movimiento = movimientoMapper.toEntity(dto);
                movimiento.setCuenta(cuenta);
                movimiento.setConcepto(concepto);

                return movimientoMapper.toResponseDTO(movimientoRepository.save(movimiento));
        }

        @Transactional
        public MovimientoResponseDTO update(Integer id, MovimientoRequestDTO dto) {
                Movimiento movimiento = movimientoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
                movimientoMapper.updateEntityFromDto(dto, movimiento);

                movimiento.setDescripcion(dto.getDescripcion());
                movimiento.setMonto(dto.getMonto());
                return movimientoMapper.toResponseDTO(movimientoRepository.save(movimiento));
        }
}
