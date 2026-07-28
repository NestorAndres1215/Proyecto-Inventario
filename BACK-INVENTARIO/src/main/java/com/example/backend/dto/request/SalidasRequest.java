package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalidasRequest {

    private Long codigoSalida;

    private Long codigoDetalle;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String observaciones;



    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @NotBlank(message = "El producto es obligatorio")
    private String producto;
    private LocalDate fechaSalida;

/*      @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
    private BigDecimal total; @NotBlank(message = "El subtotal es obligatorio")
    private BigDecimal subtotal;
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a 0")
    private BigDecimal precioUnitario;

    @Min(value = 0, message = "El stock anterior no puede ser negativo")
    private int stockAnterior;

    @Min(value = 0, message = "El stock actual no puede ser negativo")
    private int stockActual;*/
}
