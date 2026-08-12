package com.example.backend.validators;

import com.example.backend.dto.request.SalidasRequest;
import com.example.backend.entity.Producto;
import com.example.backend.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SalidaValidator {

    public void validarLista(List<SalidasRequest> detalles) {

        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("La lista de detalles no puede estar vacía.");
        }
    }

    public void validarStock(Producto producto, int cantidad) {

        int stockActual = producto.getStock() - cantidad;

        if (stockActual < 0) {
            throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
        }
    }
}