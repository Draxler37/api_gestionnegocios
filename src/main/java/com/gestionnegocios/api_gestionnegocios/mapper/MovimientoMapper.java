package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Movimiento;
import com.gestionnegocios.api_gestionnegocios.dto.Movimiento.MovimientoRequestDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {
    @Mapping(target = "idCuenta", source = "cuenta.id")
    @Mapping(target = "idTipoMovimiento", source = "tipoMovimiento.id")
    @Mapping(target = "idConcepto", source = "concepto.id")
    MovimientoResponseDTO toResponseDTO(Movimiento movimiento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cuenta", ignore = true)
    @Mapping(target = "tipoMovimiento", ignore = true)
    @Mapping(target = "concepto", ignore = true)
    @Mapping(target = "fechaMovimiento", ignore = true)
    Movimiento toEntity(MovimientoRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cuenta", ignore = true)
    @Mapping(target = "tipoMovimiento", ignore = true)
    @Mapping(target = "concepto", ignore = true)
    @Mapping(target = "fechaMovimiento", ignore = true)
    void updateEntityFromDto(MovimientoRequestDTO dto, @MappingTarget Movimiento movimiento);
}
