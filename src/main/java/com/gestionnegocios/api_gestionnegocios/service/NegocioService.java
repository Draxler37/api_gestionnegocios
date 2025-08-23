package com.gestionnegocios.api_gestionnegocios.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestionnegocios.api_gestionnegocios.dto.Negocio.NegocioRequestDTO;
import com.gestionnegocios.api_gestionnegocios.dto.Negocio.NegocioResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.NegocioMapper;
import com.gestionnegocios.api_gestionnegocios.models.Negocio;
import com.gestionnegocios.api_gestionnegocios.models.Usuario;
import com.gestionnegocios.api_gestionnegocios.repository.NegocioRepository;
import com.gestionnegocios.api_gestionnegocios.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio para manejar la lógica de negocio relacionada con los Negocios.
 * Permite obtener, crear, actualizar y desactivar negocios.
 */
@Service
@RequiredArgsConstructor
public class NegocioService {
    private final NegocioRepository negocioRepository;
    private final NegocioMapper negocioMapper;
    private final UsuarioRepository usuarioRepository;
    private final com.gestionnegocios.api_gestionnegocios.repository.CuentaRepository cuentaRepository;
    private final com.gestionnegocios.api_gestionnegocios.repository.ConceptoRepository conceptoRepository;

    /**
     * Obtiene una lista de Negocios filtrados por estado.
     * Si el estado es null, devuelve todos los Negocios.
     * Si el estado es true, devuelve solo los Negocios activos.
     * Si el estado es false, devuelve solo los Negocios inactivos.
     *
     * @param estado Estado del negocio (null, true o false).
     * @return Lista de Negocios filtrados.
     */
    public List<NegocioResponseDTO> getAll(String email, Boolean estado) {
        List<Negocio> negocios;

        if (estado == null) {
            negocios = negocioRepository.findByUsuarioEmail(email);
        } else {
            negocios = negocioRepository.findByUsuarioEmailAndEstado(email, estado);
        }

        return negocios.stream()
                .map(negocioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Agrega un nuevo negocio.
     *
     * @param negocioRequest Datos del negocio a agregar.
     * @return Negocio creado con estado 201 Created.
     */
    @Transactional
    public NegocioResponseDTO addNegocio(NegocioRequestDTO negocioRequest) {
        // Obtiene el usuario autenticado desde el contexto de seguridad
        // Asumiendo que el usuario está autenticado y tiene un email único
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Mapea el DTO a la entidad Negocio y asocia el usuario autenticado
        // Por defecto, el nuevo negocio estará activo
        Negocio negocio = negocioMapper.toEntity(negocioRequest);
        negocio.setUsuario(usuario); // Asocia el negocio al usuario autenticado
        negocio.setEstado(true); // Por defecto, el nuevo negocio está activo

        Negocio savedNegocio = negocioRepository.save(negocio);
        return negocioMapper.toResponseDTO(savedNegocio);
    }

    /**
     * Actualiza un negocio existente.
     *
     * @param id             ID del negocio a actualizar.
     * @param negocioRequest Datos actualizados del negocio.
     * @return Negocio actualizado con estado 200 OK o vacío si no existe o está
     *         inactivo.
     */
    @Transactional
    public Optional<NegocioResponseDTO> updateNegocio(Integer id, NegocioRequestDTO negocioRequest) {
        return negocioRepository.findById(id)
                .filter(Negocio::isEstado)
                .map(negocio -> {
                    // Aplica el mapper para actualizar los campos que vengan del DTO
                    negocioMapper.updateEntityFromDto(negocioRequest, negocio);
                    Negocio updatedNegocio = negocioRepository.save(negocio);
                    return negocioMapper.toResponseDTO(updatedNegocio);
                });
    }

    /**
     * Desactiva un negocio existente.
     *
     * @param id ID del negocio a desactivar.
     * @return Negocio desactivado con estado 200 OK o vacío si no existe o está
     *         inactivo.
     */
    @Transactional
    public boolean deactivateNegocio(Integer id) {
        return negocioRepository.findById(id)
                .filter(Negocio::isEstado)
                .map(negocio -> {
                    negocio.setEstado(false); // Cambia el estado a inactivo
                    negocioRepository.save(negocio);
                    return true; // Retorna true si se desactivó correctamente
                })
                .orElse(false); // Retorna false si no se encontró el negocio o ya estaba inactivo
    }

    /**
     * Activa un negocio existente.
     *
     * @param id ID del negocio a activar.
     * @return Negocio activado con estado 200 OK o vacío si no existe o está
     *         activo.
     */
    @Transactional
    public boolean activateNegocio(Integer id) {
        return negocioRepository.findById(id)
                .filter(negocio -> !negocio.isEstado()) // Verifica que esté inactivo
                .map(negocio -> {
                    negocio.setEstado(true); // Cambia el estado a activo
                    negocioRepository.save(negocio);
                    return true; // Retorna true si se activó correctamente
                })
                .orElse(false); // Retorna false si no se encontró el negocio o ya estaba activo
    }

    /**
     * Elimina un negocio por ID.
     * No permite eliminar si el negocio tiene dependencias asociadas.
     * 
     * @param id ID del negocio a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró o tiene
     *         dependencias.
     */
    @Transactional
    public boolean delete(Integer id) {
        if (!canBeDeleted(id)) {
            return false;
        }
        negocioRepository.deleteById(id);
        return true;
    }

    /**
     * Verifica si un negocio puede ser eliminado.
     *
     * @param id ID del negocio a verificar.
     * @return true si el negocio puede ser eliminado, false si no se encontró o
     *         tiene dependencias.
     */
    public boolean canBeDeleted(Integer id) {
        // Verifica si el negocio existe
        if (!negocioRepository.existsById(id)) {
            return false;
        }
        // Verifica si tiene cuentas asociadas
        if (!cuentaRepository.findByNegocioId(id).isEmpty()) {
            return false;
        }
        // Verifica si tiene conceptos asociados
        if (!conceptoRepository.findAll().stream()
                .filter(c -> c.getNegocio() != null && id.equals(c.getNegocio().getId())).findAny().isEmpty()) {
            return false;
        }
        return true;
    }
}
