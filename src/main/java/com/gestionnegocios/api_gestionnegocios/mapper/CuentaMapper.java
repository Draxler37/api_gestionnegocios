package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Cuenta;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CuentaMapper {
    @Mapping(target = "negocio.id", source = "idNegocio")
    @Mapping(target = "tipoCuenta.id", source = "idTipoCuenta")
    @Mapping(target = "moneda.id", source = "idMoneda")
    Cuenta toEntity(CuentaRequestDTO dto);

    @Mapping(target = "idNegocio", source = "negocio.id")
    @Mapping(target = "idTipoCuenta", source = "tipoCuenta.id")
    @Mapping(target = "idMoneda", source = "moneda.id")
    CuentaResponseDTO toResponseDTO(Cuenta cuenta);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "negocio.id", source = "idNegocio")
    @Mapping(target = "tipoCuenta.id", source = "idTipoCuenta")
    @Mapping(target = "moneda.id", source = "idMoneda")
    void updateEntityFromDto(CuentaRequestDTO dto, @MappingTarget Cuenta cuenta);
}
