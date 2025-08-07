package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
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
    private final MovimientoService movimientoService;

    /**
     * Obtiene una lista de cuentas filtradas por estado.
     * Si el estado es null, devuelve todas las cuentas.
     * Si el estado es true, devuelve solo las cuentas activas.
     * Si el estado es false, devuelve solo las cuentas inactivas.
     * 
     * @param estado Estado de la cuenta (null, true o false).
     * @return Lista de cuentas filtradas.
     */
    /**
     * Obtiene todas las cuentas de un negocio, filtrando por estado si se indica.
     */
    public List<CuentaResponseDTO> getAllByNegocio(Integer idNegocio, Boolean estado) {
        List<Cuenta> cuentas = cuentaRepository.findByNegocioId(idNegocio);
        return cuentas.stream()
                .filter(c -> estado == null || c.isEstado() == estado)
                .map(cuentaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crega una nueva cuenta.
     * 
     * @param dto DTO con los datos de la cuenta a crear.
     * @return DTO de la cuenta creada.
     */
    @Transactional
    public CuentaResponseDTO create(CuentaRequestDTO dto) {
        Cuenta cuenta = cuentaMapper.toEntity(dto);
        Cuenta saved = cuentaRepository.save(cuenta);
        return cuentaMapper.toResponseDTO(saved);
    }

    /**
     * Actualiza la descripción de una cuenta.
     * 
     * @param id          ID de la cuenta a actualizar.
     * @param descripcion Nueva descripción de la cuenta.
     * @return DTO de la cuenta actualizada o vacío si no se encontró.
     */
    @Transactional
    public Optional<CuentaResponseDTO> updateDescripcion(Integer id, String descripcion) {
        return cuentaRepository.findById(id).map(cuenta -> {
            cuenta.setDescripcion(descripcion);
            Cuenta updated = cuentaRepository.save(cuenta);
            return cuentaMapper.toResponseDTO(updated);
        });
    }

    /**
     * Desactiva una cuenta.
     * 
     * @param id ID de la cuenta a desactivar.
     * @return true si se desactivó correctamente, false si no se encontró.
     */
    @Transactional
    public boolean desactivar(Integer id) {
        return cuentaRepository.findById(id).map(cuenta -> {
            cuenta.setEstado(false);
            cuentaRepository.save(cuenta);
            return true;
        }).orElse(false);
    }

    /**
     * Activa una cuenta.
     * 
     * @param id ID de la cuenta a activar.
     * @return true si se activó correctamente, false si no se encontró.
     */
    @Transactional
    public boolean activar(Integer id) {
        return cuentaRepository.findById(id).map(cuenta -> {
            cuenta.setEstado(true);
            cuentaRepository.save(cuenta);
            return true;
        }).orElse(false);
    }

    /**
     * Lista los movimientos de una cuenta.
     *
     * @param idCuenta ID de la cuenta cuyos movimientos se desean listar.
     * @return Lista de movimientos de la cuenta.
     */
    public List<MovimientoResponseDTO> getMovimientos(
            Integer idCuenta) {
        return movimientoService.getByCuentaId(idCuenta);
    }
}
