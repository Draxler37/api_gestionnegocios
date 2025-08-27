package com.gestionnegocios.api_gestionnegocios.mapper;

import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Usuario;
import org.mapstruct.*;

/**
 * Mapper para convertir entre Usuario y sus DTOs.
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "negocios", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "negociosEmpleado", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);
}
