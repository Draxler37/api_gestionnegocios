package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento.TipoMovimientoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.TipoMovimiento.TipoMovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.TipoMovimiento;
import org.mapstruct.*;

/**
 * Mapper para convertir entre TipoMovimiento y sus DTOs.
 */
@Mapper(componentModel = "spring")
public interface TipoMovimientoMapper {
    TipoMovimientoResponseDTO toResponseDTO(TipoMovimiento tipoMovimiento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    @Mapping(target = "conceptos", ignore = true)
    TipoMovimiento toEntity(TipoMovimientoRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    @Mapping(target = "conceptos", ignore = true)
    void updateEntityFromDto(TipoMovimientoRequestDTO dto, @MappingTarget TipoMovimiento tipoMovimiento);
}
