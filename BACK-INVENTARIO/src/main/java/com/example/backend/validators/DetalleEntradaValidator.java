package com.example.backend.validators;

import com.example.backend.entity.DetalleEntrada;
import com.example.backend.exception.BadRequestException;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DetalleEntradaValidator {

    public void validarLista(List<DetalleEntrada> detalles) {

        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("La lista de detalles de entrada no puede estar vacía");
        }
    }

    public void validarDetalle(DetalleEntrada detalle) {

        if (detalle.getEntrada() == null || detalle.getEntrada().getFechaEntrada() == null) {
            throw new BadRequestException("Cada detalle debe incluir una fecha de entrada válida");
        }

        if (detalle.getProducto() == null || detalle.getProducto().getProductoId() == null) {
            throw new BadRequestException("Cada detalle debe contener un producto válido");
        }
    }

    public void validarCantidad(Integer cantidad) {

        if (cantidad == null || cantidad <= 0) {
            throw new BadRequestException("La cantidad debe ser mayor a cero.");
        }
    }
}