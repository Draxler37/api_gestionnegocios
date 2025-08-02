package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.Concepto.ConceptoRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Concepto.ConceptoResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Concepto;
import org.mapstruct.*;

/**
 * Mapper para convertir entre Concepto y sus DTOs.
 * Contiene métodos para mapear a ConceptoResponseDTO y ConceptoRequestDTO.
 */
@Mapper(componentModel = "spring")
public interface ConceptoMapper {
    ConceptoResponseDTO toResponseDTO(Concepto concepto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "tipoMovimiento", ignore = true)
    Concepto toEntity(ConceptoRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "tipoMovimiento", ignore = true)
    void updateEntityFromDto(ConceptoRequestDTO dto, @MappingTarget Concepto concepto);
}
