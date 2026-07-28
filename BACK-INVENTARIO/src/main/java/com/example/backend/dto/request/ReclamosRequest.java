package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReclamosRequest {

    private long codigo;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(min = 5, max = 150, message = "El asunto debe tener entre 5 y 150 caracteres")
    private String asunto;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    private String usuario;
}
