package com.example.backend.mapper;


import com.example.backend.dto.request.SalidasRequest;
import com.example.backend.entity.DetalleSalida;
import com.example.backend.entity.Producto;
import com.example.backend.entity.Salidas;
import com.example.backend.entity.Usuario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SalidaMapper {

    public Salidas toSalida(SalidasRequest dto, Usuario usuario) {

        return Salidas.builder()
                .usuario(usuario)
                .observacion(dto.getObservaciones())
                .fechaSalida(dto.getFechaSalida())
                .estado("REGISTRADO")
                .total(BigDecimal.ZERO)
                .build();
    }

    public DetalleSalida toDetalle(SalidasRequest dto, Producto producto, Usuario usuario, Salidas salida) {

        int stockAnterior = producto.getStock();
        int stockActual = stockAnterior - dto.getCantidad();

        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(dto.getCantidad()));

        return DetalleSalida.builder()
                .cantidad(dto.getCantidad())
                .descripcion(dto.getDescripcion())
                .precioUnitario(producto.getPrecio())
                .subtotal(subtotal)
                .stockAnterior(stockAnterior)
                .stockActual(stockActual)
                .usuario(usuario)
                .producto(producto)
                .salida(salida)
                .build();
    }
}