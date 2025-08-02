package com.gestionnegocios.api_gestionnegocios.service;

import com.gestionnegocios.api_gestionnegocios.dto.Login.LoginRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Login.LoginResponseDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Usuario.UsuarioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.models.Usuario;
import com.gestionnegocios.api_gestionnegocios.models.Rol;
import com.gestionnegocios.api_gestionnegocios.models.UsuarioRol;
import com.gestionnegocios.api_gestionnegocios.repository.UsuarioRepository;
import com.gestionnegocios.api_gestionnegocios.repository.RolRepository;
import com.gestionnegocios.api_gestionnegocios.repository.UsuarioRolRepository;
import com.gestionnegocios.api_gestionnegocios.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gestionnegocios.api_gestionnegocios.security.JwtUtil;
import com.gestionnegocios.api_gestionnegocios.security.TokenBlacklistService;

import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación que maneja el registro, inicio de sesión y cierre
 * de sesión de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * Registra un nuevo usuario y le asigna el rol de EMPLEADO por defecto.
     *
     * @param dto Datos del usuario a registrar.
     * @return UsuarioResponseDTO con los datos del usuario registrado.
     */
    public UsuarioResponseDTO register(UsuarioRequestDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setEstado(true);
        Usuario saved = usuarioRepository.save(usuario);

        // Asignar rol EMPLEADO por defecto
        Rol rolEmpleado = rolRepository.findByNombre("EMPLEADO")
                .orElseThrow(() -> new IllegalStateException("Rol EMPLEADO no existe"));
        UsuarioRol usuarioRol = UsuarioRol.builder()
                .usuario(saved)
                .rol(rolEmpleado)
                .build();
        usuarioRolRepository.save(usuarioRol);

        return usuarioMapper.toResponseDTO(saved);
    }

    /**
     * Inicia sesión de un usuario y genera un token JWT con su email y roles.
     *
     * @param dto Datos de inicio de sesión del usuario.
     * @return LoginResponseDTO con el token y los roles del usuario.
     */
    public Object login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos"));
        if (!usuario.isEstado()) {
            throw new IllegalStateException("Usuario inactivo");
        }
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        // Aquí recuperas los roles del usuario
        List<UsuarioRol> rolesUsuario = usuarioRolRepository.findByUsuarioId(usuario.getId());

        // Extraes solo los nombres de los roles
        List<String> nombresRoles = rolesUsuario.stream()
                .map(rol -> rol.getRol().getNombre())
                .collect(Collectors.toList());

        // Generas el token con el email y los roles
        String token = jwtUtil.generateToken(usuario.getEmail(), nombresRoles);

        return new LoginResponseDTO(token, nombresRoles, usuarioMapper.toResponseDTO(usuario));
    }

    /**
     * Cierra la sesión del usuario invalidando el token JWT.
     *
     * @param authHeader Header de autorización que contiene el token JWT.
     * @return ResponseEntity con mensaje de éxito o error.
     */
    public ResponseEntity<?> logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok().body("Sesión cerrada correctamente");
        }
        return ResponseEntity.badRequest().body("Token no proporcionado");
    }
}
