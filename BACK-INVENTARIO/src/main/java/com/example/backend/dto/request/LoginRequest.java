package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El login es obligatorio")
    private String login;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}