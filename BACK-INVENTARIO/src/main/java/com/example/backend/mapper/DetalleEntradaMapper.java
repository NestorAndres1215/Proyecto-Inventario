package com.example.backend.mapper;

import com.example.backend.entity.DetalleEntrada;
import org.springframework.stereotype.Component;

@Component
public class DetalleEntradaMapper {

    public void updateEntity(DetalleEntrada detalle, DetalleEntrada dto) {

        detalle.setCantidad(dto.getCantidad());
        detalle.setDescripcion(dto.getDescripcion());
    }
}