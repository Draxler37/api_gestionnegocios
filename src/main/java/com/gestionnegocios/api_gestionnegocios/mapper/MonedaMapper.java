package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.Moneda.MonedaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Moneda.MonedaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Moneda;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MonedaMapper {
    MonedaResponseDTO toResponseDTO(Moneda moneda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cuentas", ignore = true)
    Moneda toEntity(MonedaRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cuentas", ignore = true)
    void updateEntityFromDto(MonedaRequestDTO dto, @MappingTarget Moneda moneda);
}
