package com.example.backend.service.impl;

import com.example.backend.constants.AlreadyExistsMessages;
import com.example.backend.constants.NotFoundMessages;
import com.example.backend.dto.request.UsuarioRequest;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Usuario registrarUsuario(UsuarioRequest dto) {

        validarUsuarioNuevo(dto);

        Rol rol = obtenerRol(dto.getRol());

        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .dni(dto.getDni())
                .edad(dto.getEdad())
                .fechaNacimiento(dto.getFechaNacimiento())
                .fechaRegistro(LocalDate.now())
                .estado(true)
                .rol(rol)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Long id, UsuarioRequest dto) {

        Usuario usuario = listarPorId(id);

        validarUsuarioActualizado(usuario, dto);

        actualizarDatos(usuario, dto);

        return usuarioRepository.save(usuario);
    }

    private void actualizarDatos(Usuario usuario, UsuarioRequest dto) {

        usuario.setUsername(dto.getUsername());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setDni(dto.getDni());
        usuario.setEdad(dto.getEdad());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRol() != null) {
            usuario.setRol(obtenerRol(dto.getRol()));
        }
    }

    private void validarUsuarioNuevo(UsuarioRequest dto) {

        validarUnico(usuarioRepository.existsByUsername(dto.getUsername()), AlreadyExistsMessages.USUARIO_YA_EXISTE);

        validarUnico(usuarioRepository.existsByEmail(dto.getEmail()), AlreadyExistsMessages.CORREO_YA_EXISTE);

        validarUnico(usuarioRepository.existsByTelefono(dto.getTelefono()), AlreadyExistsMessages.TELEFONO_YA_EXISTE);

        validarUnico(usuarioRepository.existsByDni(dto.getDni()), AlreadyExistsMessages.DNI_YA_EXISTE);
    }

    private void validarUsuarioActualizado(Usuario usuario, UsuarioRequest dto) {

        validarCambio(usuario.getUsername(), dto.getUsername(), usuarioRepository::existsByUsername, AlreadyExistsMessages.USUARIO_YA_EXISTE);

        validarCambio(usuario.getEmail(), dto.getEmail(), usuarioRepository::existsByEmail, AlreadyExistsMessages.CORREO_YA_EXISTE);

        validarCambio(usuario.getTelefono(), dto.getTelefono(), usuarioRepository::existsByTelefono, AlreadyExistsMessages.TELEFONO_YA_EXISTE);

        validarCambio(usuario.getDni(), dto.getDni(), usuarioRepository::existsByDni, AlreadyExistsMessages.DNI_YA_EXISTE);
    }

    private void validarCambio(String valorActual, String nuevoValor, Predicate<String> existe, String mensaje) {

        if (nuevoValor != null && !Objects.equals(valorActual, nuevoValor) && existe.test(nuevoValor)) {
            throw new ResourceAlreadyExistsException(mensaje);
        }
    }

    private void validarUnico(boolean existe, String mensaje) {
        if (existe) {
            throw new ResourceAlreadyExistsException(mensaje);
        }
    }

    private Rol obtenerRol(String nombreRol) {
        return rolRepository.findByNombre(nombreRol)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.ROL_NO_ENCONTRADO));
    }

    @Override
    public Usuario eliminarUsuario(Long usuarioId) {
        return cambiarEstadoUsuario(usuarioId, false);
    }

    @Override
    public Usuario activarUsuario(Long usuarioId) {
        return cambiarEstadoUsuario(usuarioId, true);
    }

    @Override
    public Usuario cambiarEstadoUsuario(Long usuarioId, boolean estado) {
        Usuario usuario = listarPorId(usuarioId);
        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario listarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.USUARIO_NO_ENCONTRADO));
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.USUARIO_NO_ENCONTRADO));
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.USUARIO_NO_ENCONTRADO));
    }

    @Override
    public Usuario buscarPorTelefono(String telefono) {
        return usuarioRepository.findByTelefono(telefono)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.USUARIO_NO_ENCONTRADO));
    }

    @Override
    public Usuario buscarPorDni(String dni) {
        return usuarioRepository.findByDni(dni)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.USUARIO_NO_ENCONTRADO));
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

    @Override
    public boolean usuarioExistePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public boolean usuarioExistePorCorreo(String correo) {
        return usuarioRepository.existsByEmail(correo);
    }

    @Override
    public boolean usuarioExistePorTelefono(String telefono) {
        return usuarioRepository.existsByTelefono(telefono);
    }

    @Override
    public boolean usuarioExistePorDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }
}