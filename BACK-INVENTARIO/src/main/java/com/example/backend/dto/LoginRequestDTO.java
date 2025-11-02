package com.example.backend.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "El login es obligatorio")
    private String login;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}