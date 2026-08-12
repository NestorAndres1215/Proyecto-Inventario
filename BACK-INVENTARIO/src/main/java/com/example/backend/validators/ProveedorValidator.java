package com.example.backend.validators;

import com.example.backend.dto.request.ProveedorRequest;
import com.example.backend.entity.Proveedor;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ProveedorValidator {

    private final ProveedorRepository proveedorRepository;

    public void validarProveedorNuevo(ProveedorRequest dto) {

        validarUnico(
                proveedorRepository.existsByRuc(dto.getRuc()),
                "El RUC ya existe"
        );

        validarUnico(
                proveedorRepository.existsByEmail(dto.getEmail()),
                "El email ya existe"
        );

        validarUnico(
                proveedorRepository.existsByTelefono(dto.getTelefono()),
                "El teléfono ya existe"
        );
    }

    public void validarProveedorActualizado(
            Proveedor proveedor,
            ProveedorRequest dto
    ) {

        validarCambio(
                proveedor.getRuc(),
                dto.getRuc(),
                proveedorRepository::existsByRuc,
                "El RUC ya existe"
        );

        validarCambio(
                proveedor.getEmail(),
                dto.getEmail(),
                proveedorRepository::existsByEmail,
                "El email ya existe"
        );

        validarCambio(
                proveedor.getTelefono(),
                dto.getTelefono(),
                proveedorRepository::existsByTelefono,
                "El teléfono ya existe"
        );
    }

    private void validarUnico(boolean existe, String mensaje) {
        if (existe) {
            throw new ResourceAlreadyExistsException(mensaje);
        }
    }

    private void validarCambio(String valorActual, String nuevoValor, Predicate<String> existe, String mensaje) {

        if (nuevoValor != null
                && !Objects.equals(valorActual, nuevoValor)
                && existe.test(nuevoValor)) {

            throw new ResourceAlreadyExistsException(mensaje);
        }
    }
}