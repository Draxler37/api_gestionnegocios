package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta.TipoCuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.TipoCuenta.TipoCuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.TipoCuenta;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TipoCuentaMapper {
    TipoCuentaResponseDTO toResponseDTO(TipoCuenta tipoCuenta);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "cuentas", ignore = true)
    TipoCuenta toEntity(TipoCuentaRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "cuentas", ignore = true)
    void updateEntityFromDto(TipoCuentaRequestDTO dto, @MappingTarget TipoCuenta tipoCuenta);
}
