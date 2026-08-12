package com.example.backend.service.impl;

import com.example.backend.dto.request.UsuarioRequest;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.UsuarioMapper;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.UsuarioService;
import com.example.backend.validators.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsuarioValidator usuarioValidator;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario registrarUsuario(UsuarioRequest dto) {

        usuarioValidator.validarUsuarioNuevo(dto);

        Rol rol = obtenerRol(dto.getRol());

        Usuario usuario = usuarioMapper.toEntity(dto, rol);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Long id, UsuarioRequest dto) {

        Usuario usuario = listarPorId(id);
        usuarioValidator.validarUsuarioActualizado(usuario, dto);
        return usuarioRepository.save(usuario);
    }


    @Override
    public Usuario eliminarUsuario(Long usuarioId) {
        return cambiarEstadoUsuario(usuarioId, false);
    }

    @Override
    public Usuario activarUsuario(Long usuarioId) {
        return cambiarEstadoUsuario(usuarioId, true);
    }


    public Usuario cambiarEstadoUsuario(Long usuarioId, boolean estado) {

        Usuario usuario = listarPorId(usuarioId);

        usuario.setEstado(estado);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario listarPorId(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );
    }

    @Override
    public Usuario buscarPorUsername(String username) {

        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );
    }

    @Override
    public Usuario buscarPorEmail(String email) {

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );
    }

    @Override
    public Usuario buscarPorTelefono(String telefono) {

        return usuarioRepository.findByTelefono(telefono)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );
    }

    @Override
    public Usuario buscarPorDni(String dni) {

        return usuarioRepository.findByDni(dni)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );
    }

    @Override
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Usuario> buscarPorApellido(String apellido) {
        return usuarioRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    @Override
    public List<Usuario> usuariosActivos() {
        return usuarioRepository.findByEstadoTrue();
    }

    @Override
    public List<Usuario> usuariosInactivos() {
        return usuarioRepository.findByEstadoFalse();
    }

    @Override
    public List<Usuario> buscarPorRolYEstado(Rol rol, boolean estado) {
        return usuarioRepository.findByRolAndEstado(rol, estado);
    }

    @Override
    public Rol getRolByNombre(String nombre) {
        return obtenerRol(nombre);
    }

    @Override
    public List<Usuario> listarUsuarioAdminActivado() {
        return usuarioRepository.listarUsuarioAdminActivado();
    }

    @Override
    public List<Usuario> listarUsuarioAdminDesactivado() {
        return usuarioRepository.listarUsuarioAdminDesactivado();
    }

    @Override
    public List<Usuario> listarUsuarioNormalActivado() {
        return usuarioRepository.listarUsuarioNormalActivado();
    }

    @Override
    public List<Usuario> listarUsuarioNormalDesactivado() {
        return usuarioRepository.listarUsuarioNormalDesactivado();
    }

    private Rol obtenerRol(String nombreRol) {

        return rolRepository.findByNombre(nombreRol)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado")
                );
    }
}