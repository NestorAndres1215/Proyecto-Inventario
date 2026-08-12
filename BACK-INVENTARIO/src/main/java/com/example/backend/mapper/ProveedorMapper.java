package com.example.backend.mapper;

import com.example.backend.dto.request.ProveedorRequest;
import com.example.backend.entity.Proveedor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProveedorMapper {

    public Proveedor toEntity(ProveedorRequest dto) {

        return Proveedor.builder()
                .nombre(dto.getNombre())
                .ruc(dto.getRuc())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .contacto("")
                .fechaRegistro(LocalDate.now())
                .estado(true)
                .build();
    }

    public void updateEntity(Proveedor proveedor, ProveedorRequest dto) {

        proveedor.setNombre(dto.getNombre());
        proveedor.setRuc(dto.getRuc());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
    }
}