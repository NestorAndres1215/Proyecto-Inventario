package com.example.backend.mapper;

import com.example.backend.dto.request.ProductoRequest;
import com.example.backend.entity.Producto;
import com.example.backend.entity.Proveedor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest dto, Proveedor proveedor) {

        return Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .descripcion(dto.getDescripcion())
                .ubicacion(dto.getUbicacion())
                .stock(dto.getStock())
                .estado(true)
                .fechaRegistro(LocalDate.now())
                .proveedor(proveedor)
                .build();
    }

    public void updateEntity(Producto producto, ProductoRequest dto, Proveedor proveedor) {

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setUbicacion(dto.getUbicacion());
        producto.setStock(dto.getStock());
        producto.setProveedor(proveedor);
    }
}