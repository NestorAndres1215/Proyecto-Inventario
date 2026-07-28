package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El precio es obligatorio")
    @Pattern(
            regexp = "^\\d+(\\.\\d{1,2})?$",
            message = "El precio debe ser un número válido (ej: 10 o 10.50)"
    )
    private BigDecimal precio;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre 5 y 255 caracteres")
    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    private String ubicacion;

    @NotNull(message = "El proveedor es obligatorio")
    private Long proveedorId;

}
