package com.example.backend.service;

import com.example.backend.dto.request.UsuarioRequest;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario registrarUsuario(UsuarioRequest dto);

    Usuario actualizarUsuario(Long id, UsuarioRequest dto);

    Usuario eliminarUsuario(Long usuarioId);

    Usuario activarUsuario(Long usuarioId);

    Usuario cambiarEstadoUsuario(Long usuarioId, boolean estado);

    Usuario listarPorId(Long id);

    Usuario buscarPorUsername(String username);

    Usuario buscarPorEmail(String email);

    Usuario buscarPorTelefono(String telefono);

    Usuario buscarPorDni(String dni);

    List<Usuario> buscarPorNombre(String nombre);

    List<Usuario> buscarPorApellido(String apellido);

    List<Usuario> usuariosActivos();

    List<Usuario> usuariosInactivos();

    List<Usuario> buscarPorRolYEstado(Rol rol, boolean estado);

    Rol getRolByNombre(String nombre);

    List<Usuario> listarUsuarioAdminActivado();

    List<Usuario> listarUsuarioAdminDesactivado();

    List<Usuario> listarUsuarioNormalActivado();

    List<Usuario> listarUsuarioNormalDesactivado();

    boolean usuarioExistePorUsername(String username);

    boolean usuarioExistePorCorreo(String correo);

    boolean usuarioExistePorTelefono(String telefono);

    boolean usuarioExistePorDni(String dni);
}