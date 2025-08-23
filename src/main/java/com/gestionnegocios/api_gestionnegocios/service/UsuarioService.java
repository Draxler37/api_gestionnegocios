package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioPasswordUpdateDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.UsuarioMapper;
import com.gestionnegocios.api_gestionnegocios.models.Rol;
import com.gestionnegocios.api_gestionnegocios.models.Usuario;
import com.gestionnegocios.api_gestionnegocios.models.UsuarioRol;
import com.gestionnegocios.api_gestionnegocios.repository.RolRepository;
import com.gestionnegocios.api_gestionnegocios.repository.UsuarioRepository;
import com.gestionnegocios.api_gestionnegocios.repository.UsuarioRolRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final UsuarioMapper usuarioMapper;

    /**
     * Obtiene una lista de usuarios filtrados por estado.
     * Si el estado es null, devuelve todos los usuarios.
     * Si el estado es true, devuelve solo los activos.
     * Si el estado es false, devuelve solo los inactivos.
     *
     * @param estado Estado del usuario (null, true o false).
     * @return Lista de usuarios filtrados.
     */
    public List<UsuarioResponseDTO> getAll(Boolean estado) {
        List<Usuario> usuario;

        if (estado == null) {
            usuario = usuarioRepository.findAll();
        } else {
            usuario = usuarioRepository.findByEstado(estado);
        }

        return usuario.stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el usuario autenticado.
     *
     * @return Usuario encontrado o vacío si no existe o está inactivo.
     */
    public Optional<UsuarioResponseDTO> getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        return usuarioRepository.findByEmail(email)
                .filter(Usuario::isEstado)
                .map(usuarioMapper::toResponseDTO);
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param dto DTO con los datos del usuario a crear.
     * @return Usuario creado.
     */
    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setEstado(true); // Activo por defecto
        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(saved);
    }

    /**
     * Actualiza la contraseña del usuario.
     *
     * @param id  ID del usuario a actualizar.
     * @param dto DTO con la contraseña antigua y la nueva.
     * @return Usuario actualizado o vacío si no existe o la contraseña es
     *         incorrecta.
     */
    @Transactional
    public Optional<UsuarioResponseDTO> updatePassword(UsuarioPasswordUpdateDTO dto) {
        // Obtener el usuario autenticado desde el contexto de seguridad
        String email = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        return usuarioRepository.findByEmail(email)
                .filter(Usuario::isEstado)
                .map(usuario -> {
                    // Validar la contraseña antigua
                    if (!BCrypt.checkpw(dto.getOldPassword(), usuario.getPassword())) {
                        throw new IllegalArgumentException("La contraseña actual es incorrecta");
                    }
                    // Actualizar la nueva contraseña
                    usuario.setPassword(BCrypt.hashpw(dto.getNewPassword(), BCrypt.gensalt()));
                    Usuario updated = usuarioRepository.save(usuario);
                    return usuarioMapper.toResponseDTO(updated);
                });
    }

    /**
     * Desactiva un usuario (marca como inactivo).
     *
     * @param id ID del usuario a eliminar.
     * @return true si se desactivó correctamente, false si no existe o ya está
     *         inactivo.
     */
    @Transactional
    public boolean deactivateUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .filter(Usuario::isEstado)
                .map(usuario -> {
                    usuario.setEstado(false);
                    usuarioRepository.save(usuario);
                    return true;
                }).orElse(false);
    }

    /**
     * Activa un usuario (marca como activo).
     *
     * @param id ID del usuario a activar.
     * @return Usuario activado o vacío si no existe o ya está activo.
     */
    @Transactional
    public Optional<UsuarioResponseDTO> activarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .filter(usuario -> !usuario.isEstado())
                .map(usuario -> {
                    usuario.setEstado(true);
                    Usuario actualizado = usuarioRepository.save(usuario);
                    return usuarioMapper.toResponseDTO(actualizado);
                });
    }

    /**
     * Asigna un rol a un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idRol     ID del rol a asignar.
     * @return true si se asignó correctamente, false si el usuario o rol no existen
     *         o ya tiene el rol.
     */
    @Transactional
    public boolean addRolToUsuario(Integer idUsuario, Integer idRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return false;
        }

        Rol rol = rolRepository.findById(idRol).orElse(null);
        if (rol == null) {
            return false;
        }

        // Verificar si ya tiene el rol
        boolean exists = usuarioRolRepository.findByUsuarioId(idUsuario)
                .stream().anyMatch(ur -> ur.getRol().getId().equals(idRol));
        if (exists) {
            return false;
        }

        UsuarioRol usuarioRol = UsuarioRol.builder().usuario(usuario).rol(rol).build();
        usuarioRolRepository.save(usuarioRol);
        return true;
    }

    /**
     * Elimina un rol de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idRol     ID del rol a eliminar.
     * @return true si se eliminó correctamente, false si el usuario o rol no
     *         existen o no tiene el rol.
     */
    @Transactional
    public boolean removeRolFromUsuario(Integer idUsuario, Integer idRol) {
        List<UsuarioRol> usuarioRoles = usuarioRolRepository.findByUsuarioId(idUsuario);
        UsuarioRol usuarioRol = usuarioRoles.stream()
                .filter(ur -> ur.getRol().getId().equals(idRol)).findFirst().orElse(null);
        if (usuarioRol == null)
            return false;

        usuarioRolRepository.delete(usuarioRol);
        return true;
    }
}
