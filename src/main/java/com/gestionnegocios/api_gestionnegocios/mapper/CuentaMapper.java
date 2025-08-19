package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Cuenta;

import org.mapstruct.*;

/**
 * Mapper para convertir entre Cuenta y sus DTOs.
 * Sigue el patrón profesional: ignora relaciones y autogenerados en toEntity y
 * update.
 */
@Mapper(componentModel = "spring")
public interface CuentaMapper {
    @Mapping(target = "idNegocio", source = "negocio.id")
    @Mapping(target = "idTipoCuenta", source = "tipoCuenta.id")
    @Mapping(target = "idMoneda", source = "moneda.id")
    CuentaResponseDTO toResponseDTO(Cuenta cuenta);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "tipoCuenta", ignore = true)
    @Mapping(target = "moneda", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Cuenta toEntity(CuentaRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "tipoCuenta", ignore = true)
    @Mapping(target = "moneda", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    void updateEntityFromDto(CuentaRequestDTO dto, @MappingTarget Cuenta cuenta);
}
