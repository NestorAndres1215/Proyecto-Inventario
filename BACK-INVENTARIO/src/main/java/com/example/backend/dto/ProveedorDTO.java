package com.example.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO {

    private Long id;
    private String nombre;
    private String ruc;
    private String direccion;
    private String telefono;
    private String email;
}
