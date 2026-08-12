package com.example.backend.validators;

import com.example.backend.dto.request.UsuarioRequest;
import com.example.backend.entity.Usuario;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    public void validarUsuarioNuevo(UsuarioRequest dto) {

        validarUnico(
                usuarioRepository.existsByUsername(dto.getUsername()),
                "El username ya existe"
        );

        validarUnico(
                usuarioRepository.existsByEmail(dto.getEmail()),
                "El email ya existe"
        );

        validarUnico(
                usuarioRepository.existsByTelefono(dto.getTelefono()),
                "El teléfono ya existe"
        );

        validarUnico(
                usuarioRepository.existsByDni(dto.getDni()),
                "El DNI ya existe"
        );
    }

    public void validarUsuarioActualizado(
            Usuario usuario,
            UsuarioRequest dto
    ) {

        validarCambio(
                usuario.getUsername(),
                dto.getUsername(),
                usuarioRepository::existsByUsername,
                "El username ya existe"
        );

        validarCambio(
                usuario.getEmail(),
                dto.getEmail(),
                usuarioRepository::existsByEmail,
                "El email ya existe"
        );

        validarCambio(
                usuario.getTelefono(),
                dto.getTelefono(),
                usuarioRepository::existsByTelefono,
                "El teléfono ya existe"
        );

        validarCambio(
                usuario.getDni(),
                dto.getDni(),
                usuarioRepository::existsByDni,
                "El DNI ya existe"
        );
    }

    private void validarUnico(
            boolean existe,
            String mensaje
    ) {
        if (existe) {
            throw new ResourceAlreadyExistsException(mensaje);
        }
    }

    private void validarCambio(
            String valorActual,
            String nuevoValor,
            Predicate<String> existe,
            String mensaje
    ) {

        if (nuevoValor != null
                && !Objects.equals(valorActual, nuevoValor)
                && existe.test(nuevoValor)) {

            throw new ResourceAlreadyExistsException(mensaje);
        }
    }
}