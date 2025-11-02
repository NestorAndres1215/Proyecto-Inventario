package com.example.backend.service;


import com.example.backend.repository.RolRepository;
import com.example.backend.dto.UsuarioRequestDTO;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.utils.ValidacionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RolRepository rolRepository;

    // Constantes para mensajes
    private static final String MSG_USUARIO_EXISTE = "El usuario ya existe";
    private static final String MSG_CORREO_EXISTE = "El correo ya existe";
    private static final String MSG_TELEFONO_EXISTE = "El teléfono ya existe";
    private static final String MSG_TELEFONO_INVALIDO = "El teléfono debe tener 9 dígitos";
    private static final String MSG_CORREO_INVALIDO = "El correo no tiene un formato válido";
    private static final String MSG_EDAD_MINIMA = "La edad mínima permitida es 18 años";
    private static final String MSG_ROL_NO_EXISTE = "El rol %s no existe en la base de datos";
    private static final String MSG_USUARIO_NO_ENCONTRADO = "Usuario con ID %d no encontrado";

    public Usuario registrarUsuario(UsuarioRequestDTO usuarioDTO) {
        validarUsuario(usuarioDTO);

        Rol rol = rolRepository.findByNombre(usuarioDTO.getRol())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format(MSG_ROL_NO_EXISTE, usuarioDTO.getRol()))
                );

        Usuario usuario = Usuario.builder()
                .username(usuarioDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(usuarioDTO.getPassword()))
                .nombre(usuarioDTO.getNombre())
                .apellido(usuarioDTO.getApellido())
                .email(usuarioDTO.getEmail())
                .telefono(usuarioDTO.getTelefono())
                .direccion(usuarioDTO.getDireccion())
                .dni(usuarioDTO.getDni())
                .edad(usuarioDTO.getEdad())
                .fechaNacimiento(usuarioDTO.getFechaNacimiento())
                .fechaRegistro(LocalDate.now())
                .estado(true)
                .rol(rol)
                .build();

        return usuarioRepository.save(usuario);
    }

    private static final String MSG_DNI_EXISTE = "El DNI ya existe";

    private void validarUsuario(UsuarioRequestDTO dto) {
        if (usuarioExistePorUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, MSG_USUARIO_EXISTE);
        }
        if (usuarioExistePorCorreo(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, MSG_CORREO_EXISTE);
        }
        if (usuarioExistePorTelefono(dto.getTelefono())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, MSG_TELEFONO_EXISTE);
        }
        if (usuarioExistePorDni(dto.getDni())) { // ← validación DNI
            throw new ResponseStatusException(HttpStatus.CONFLICT, MSG_DNI_EXISTE);
        }

        if (!ValidacionUtil.TelefonoValido(dto.getTelefono())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_TELEFONO_INVALIDO);
        }
        if (!ValidacionUtil.CorreoValido(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_CORREO_INVALIDO);
        }
        if (dto.getEdad() < 18) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_EDAD_MINIMA);
        }
    }


    public Usuario eliminarUsuario(Long usuarioId) {
        return cambiarEstadoUsuario(usuarioId, false);
    }

    public Usuario activarUsuario(Long usuarioId) {
        return cambiarEstadoUsuario(usuarioId, true);
    }

    private Usuario cambiarEstadoUsuario(Long usuarioId, boolean estado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format(MSG_USUARIO_NO_ENCONTRADO, usuarioId)
                        )
                );

        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }

    //LISTAR POR CODIGO

    public Optional<Usuario> listarCodigo(String codigo) {
        return usuarioRepository.findById(codigo);
    }

    //LISTAR POR USUARIOS
    public List<Usuario> listarUsuarioAdminActivado() {
        return usuarioRepository.listarUsuarioAdminActivado();
    }


    public List<Usuario> listarUsuarioAdminDesactivado() {
        return usuarioRepository.listarUsuarioAdminDesactivado();
    }


    public List<Usuario> listarUsuarioNormalActivado() {
        return usuarioRepository.listarUsuarioNormalActivado();
    }


    public List<Usuario> listarUsuarioNormalDesactivado() {
        return usuarioRepository.listarUsuarioNormalDesactivado();
    }


    public Usuario listarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }


    // MÉTODOS EXISTE
    public boolean usuarioExistePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean usuarioExistePorCorreo(String correo) {
        return usuarioRepository.existsByEmail(correo);
    }

    public boolean usuarioExistePorTelefono(String telefono) {
        return usuarioRepository.existsByTelefono(telefono);
    }

    public boolean usuarioExistePorDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }

    //METODO DE LISTAR
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format(MSG_USUARIO_NO_ENCONTRADO, id))
                );
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Usuario con username '" + username + "' no encontrado")
                );
    }

    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Usuario> buscarPorApellido(String apellido) {
        return usuarioRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    public Usuario buscarPorDni(String dni) {
        return usuarioRepository.findByDni(dni)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Usuario con DNI '" + dni + "' no encontrado")
                );
    }

    public List<Usuario> usuariosActivos() {
        return usuarioRepository.findByEstadoTrue()
                .map(List::of).orElse(List.of());
    }

    public List<Usuario> usuariosInactivos() {
        return usuarioRepository.findByEstadoFalse()
                .map(List::of).orElse(List.of());
    }

    public List<Usuario> buscarPorRolYEstado(Rol rol, boolean estado) {
        List<Usuario> usuarios = usuarioRepository.findByRolAndEstado(rol, estado);
        if (usuarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No se encontraron usuarios con rol '%s' y estado '%s'", rol.getNombre(), estado));
        }
        return usuarios;
    }

    public Rol getRolByNombre(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Rol '%s' no encontrado", nombre)
                ));
    }
    public Optional<Usuario> buscarPorTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }
        return usuarioRepository.findByTelefono(telefono.trim());
    }

    // ------------------ BUSCAR POR EMAIL ------------------
    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }
        return usuarioRepository.findByEmail(email.trim());
    }
}
