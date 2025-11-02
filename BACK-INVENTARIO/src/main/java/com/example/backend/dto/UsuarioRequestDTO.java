package com.example.backend.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "El email debe ser válido")
    private String email;

    private String telefono;
    private String direccion;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    private Integer edad;

    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;
}
