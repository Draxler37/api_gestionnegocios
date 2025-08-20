package com.gestionnegocios.api_gestionnegocios.security;

import com.gestionnegocios.api_gestionnegocios.repository.UsuarioRepository;
import com.gestionnegocios.api_gestionnegocios.repository.RolRepository;
import com.gestionnegocios.api_gestionnegocios.models.Rol;
import com.gestionnegocios.api_gestionnegocios.models.Usuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    public JwtAuthenticationFilter(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Verificar si el token está en la blacklist
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido o expirado");
                return;
            }

            // Validar el token y establecer la autenticación
            if (jwtUtil.isTokenValid(token)) {
                String username = jwtUtil.getUsername(token);
                List<String> rolesToken = jwtUtil.getRoles(token);

                // 🔎 Validar si el usuario existe y está activo
                Usuario usuario = usuarioRepository.findByEmail(username).orElse(null);
                if (usuario == null || !usuario.isEstado()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Usuario inactivo");
                    return;
                }

                // 🔎 Validar si los roles siguen activos en la BD
                List<String> rolesActivos = rolesToken.stream()
                        .filter(role -> rolRepository.findByNombre(role)
                                .map(Rol::isEstado)   // Solo pasa si el rol sigue activo
                                .orElse(false))
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .collect(Collectors.toList());

                if (rolesActivos.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Usuario sin roles válidos");
                    return;
                }

                // Crear la autenticación con roles activos
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null,
                                rolesActivos.stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList()));

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
