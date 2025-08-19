package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento.TipoMovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.TipoMovimiento;
import org.mapstruct.*;

/**
 * Mapper para convertir entre TipoMovimiento y sus DTOs.
 */
@Mapper(componentModel = "spring")
public interface TipoMovimientoMapper {
    TipoMovimientoResponseDTO toResponseDTO(TipoMovimiento tipoMovimiento);
}
