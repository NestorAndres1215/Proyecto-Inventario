package com.example.backend.service.impl;


import com.example.backend.repository.RolRepository;
import com.example.backend.dto.UsuarioRequestDTO;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.UsuarioService;
import com.example.backend.utils.ValidacionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;


    private  final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final  RolRepository rolRepository;



    @Override
    public Usuario registrarUsuario(UsuarioRequestDTO usuarioDTO) {
        validarUsuario(usuarioDTO);
        Rol rolAdmin = rolRepository.findByNombre(usuarioDTO.getRol())
                .orElseThrow(() -> new RuntimeException("El rol ADMIN no existe en la BD"));
        System.out.println(rolAdmin.getCodigo());
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(bCryptPasswordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setDireccion(usuarioDTO.getDireccion());
        usuario.setDni(usuarioDTO.getDni());
        usuario.setEdad(usuarioDTO.getEdad());
        usuario.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setEstado(true);
        usuario.setRol(rolAdmin); // creas un Rol nuevo en memoria
        return usuarioRepository.save(usuario);
    }

    private boolean validarUsuario(UsuarioRequestDTO normalDTO) {

        if (usuarioExistePorUsername(normalDTO.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (usuarioExistePorCorreo(normalDTO.getEmail())) {
            throw new IllegalArgumentException("El correo ya existe");
        }
        if (usuarioExistePorTelefono(normalDTO.getTelefono())) {
            throw new IllegalArgumentException("El teléfono ya existe");
        }

        if (!ValidacionUtil.TelefonoValido(normalDTO.getTelefono())) {
            throw new IllegalArgumentException("El teléfono debe tener 9 dígitos");
        }
        if (!ValidacionUtil.CorreoValido(normalDTO.getEmail())) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }

        if (normalDTO.getEdad() < 18) {
            throw new IllegalArgumentException("La edad mínima permitida es 18 años");
        }

        return true;
    }


    @Override
    public Usuario eliminarUsuario(Long usuarioId) {
        return cambiarEstadoUsuario(usuarioId, false);
    }

    @Override
    public Usuario activarUsuario(Long usuarioId) {
        return cambiarEstadoUsuario(usuarioId, true);
    }


    private Usuario cambiarEstadoUsuario(Long usuarioId, boolean estado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario con ID " + usuarioId + " no encontrado"));

        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }


    //LISTAR POR CODIGO
    @Override
    public Optional<Usuario> listarCodigo(String codigo) {
        return usuarioRepository.findById(codigo);
    }

    //LISTAR POR USUARIOS
    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    //EXISTE USUARIO Y CONTRASEÑA
    @Override
    public boolean existsByUsernameAndPassword(String username, String password) {
        return usuarioRepository.existsByUsernameAndPassword(username, password);
    }

    //EXISTE USUARIO
    @Override
    public boolean usuarioExistePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    //EXISTE CORREO
    @Override
    public boolean usuarioExistePorCorreo(String correo) {
        return usuarioRepository.existsByEmail(correo);
    }

    //EXISTE TELEFONO
    @Override
    public boolean usuarioExistePorTelefono(String telefono) {
        return usuarioRepository.existsByTelefono(telefono);
    }

    //EXISTE DNI
    @Override
    public boolean usuarioExistePorDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }


    @Override
    public List<Usuario> obtenerUsuariosPorRol(Long codigo, Boolean estado) {
        Rol rol = rolRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + codigo));

        return usuarioRepository.findByRolAndEstado(rol, estado);
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
    public Usuario listarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

}
