package com.example.backend.mapper;

import com.example.backend.dto.request.UsuarioRequest;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UsuarioMapper {

    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioMapper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario toEntity(UsuarioRequest dto, Rol rol) {

        return Usuario.builder()
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
    }

    public void updateEntity(
            Usuario usuario,
            UsuarioRequest dto,
            Rol rol
    ) {

        usuario.setUsername(dto.getUsername());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setDni(dto.getDni());
        usuario.setEdad(dto.getEdad());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());

        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {

            usuario.setPassword(
                    passwordEncoder.encode(dto.getPassword())
            );
        }

        if (rol != null) {
            usuario.setRol(rol);
        }
    }
}