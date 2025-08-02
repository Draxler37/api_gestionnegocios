package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.Negocio.NegocioRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Negocio.NegocioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Negocio;
import org.mapstruct.*;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper para convertir entre Negocio y sus DTOs.
 * Contiene métodos para mapear a NegocioResponseDTO y NegocioRequestDTO.
 */
@Mapper(componentModel = "spring")
public interface NegocioMapper {
    NegocioResponseDTO toResponseDTO(Negocio negocio);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "cuentas", ignore = true)
    @Mapping(target = "conceptos", ignore = true)
    Negocio toEntity(NegocioRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "cuentas", ignore = true)
    @Mapping(target = "conceptos", ignore = true)
    void updateEntityFromDto(NegocioRequestDTO dto, @MappingTarget Negocio negocio);
}
