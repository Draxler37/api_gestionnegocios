package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.Rol.RolRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Rol.RolResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Rol;
import org.mapstruct.*;

/**
 * Mapper para convertir entre Rol y sus DTOs.
 */
@Mapper(componentModel = "spring")
public interface RolMapper {
    RolResponseDTO toResponseDTO(Rol rol);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    Rol toEntity(RolRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    void updateEntityFromDto(RolRequestDTO dto, @MappingTarget Rol rol);
}
