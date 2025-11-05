package com.example.backend.service;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.entity.Proveedor;
import com.example.backend.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    // ------------------ CONSTANTES ------------------
    private static final String MENSAJE_NOMBRE_OBLIGATORIO = "El nombre del proveedor es obligatorio.";
    private static final String MENSAJE_RUC_OBLIGATORIO = "El RUC del proveedor es obligatorio.";
    private static final String MENSAJE_RUC_INVALIDO = "El RUC debe contener exactamente 11 dígitos numéricos.";
    private static final String MENSAJE_EMAIL_INVALIDO = "El formato del correo electrónico no es válido.";
    private static final String MENSAJE_TELEFONO_INVALIDO = "El número de teléfono debe tener entre 7 y 15 dígitos.";
    private static final String MENSAJE_DUPLICADO_RUC = "Ya existe un proveedor con el mismo RUC.";
    private static final String MENSAJE_PROVEEDOR_NO_ENCONTRADO = "Proveedor con ID %d no encontrado.";

    private static final String PATRON_RUC = "\\d{11}";
    private static final String PATRON_EMAIL = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
    private static final String PATRON_TELEFONO = "^\\d{7,15}$";

    // ------------------ LISTAR ------------------
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    public List<Proveedor> findByEstadoIsTrue() {
        return proveedorRepository.findByEstadoIsTrue();
    }

    public List<Proveedor> findByEstadoIsFalse() {
        return proveedorRepository.findByEstadoIsFalse();
    }

    public Optional<Proveedor> obtenerPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    // ------------------ CREAR ------------------
    public Proveedor crearProveedor(ProveedorDTO proveedorDTO) {

        validarDatosProveedor(proveedorDTO);


        Proveedor proveedor = Proveedor.builder()
                .nombre(proveedorDTO.getNombre())
                .ruc(proveedorDTO.getRuc())
                .direccion(proveedorDTO.getDireccion())
                .telefono(proveedorDTO.getTelefono())
                .email(proveedorDTO.getEmail())
                .estado(true)
                .build();

        return proveedorRepository.save(proveedor);
    }

    // ------------------ ACTUALIZAR ------------------
    public Proveedor actualizarProveedor(ProveedorDTO proveedorDTO) {

        Proveedor proveedor = proveedorRepository.findById(proveedorDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException(MENSAJE_PROVEEDOR_NO_ENCONTRADO));

        validarDatosProveedor(proveedorDTO);

        // Evitar duplicados de RUC si cambia
        proveedorRepository.findByRuc(proveedorDTO.getRuc()).ifPresent(existing -> {
            if (!existing.getProveedorId().equals(proveedorDTO.getId())) {
                throw new IllegalStateException(MENSAJE_DUPLICADO_RUC);
            }
        });

        proveedor.setNombre(proveedorDTO.getNombre());
        proveedor.setRuc(proveedorDTO.getRuc());
        proveedor.setDireccion(proveedorDTO.getDireccion());
        proveedor.setTelefono(proveedorDTO.getTelefono());
        proveedor.setEmail(proveedorDTO.getEmail());

        return proveedorRepository.save(proveedor);
    }

    // ------------------ ACTIVAR / DESACTIVAR ------------------
    public boolean activarProveedor(Long id) {
        return cambiarEstadoProveedor(id, true);
    }

    public boolean desactivarProveedor(Long id) {
        return cambiarEstadoProveedor(id, false);
    }

    private boolean cambiarEstadoProveedor(Long id, boolean estado) {
        return proveedorRepository.findById(id)
                .map(proveedor -> {
                    proveedor.setEstado(estado);
                    proveedorRepository.save(proveedor);
                    return true;
                })
                .orElse(false);
    }

    // ------------------ VALIDACIONES ------------------
    private void validarDatosProveedor(ProveedorDTO proveedorDTO) {
        if (proveedorDTO.getNombre() == null || proveedorDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_NOMBRE_OBLIGATORIO);
        }

        if (proveedorDTO.getRuc() == null || proveedorDTO.getRuc().trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_RUC_OBLIGATORIO);
        }

        if (!proveedorDTO.getRuc().matches(PATRON_RUC)) {
            throw new IllegalArgumentException(MENSAJE_RUC_INVALIDO);
        }

        if (proveedorDTO.getEmail() != null && !proveedorDTO.getEmail().trim().isEmpty() && !proveedorDTO.getEmail().matches(PATRON_EMAIL)) {
            throw new IllegalArgumentException(MENSAJE_EMAIL_INVALIDO);
        }

        if (proveedorDTO.getTelefono() != null && !proveedorDTO.getTelefono().trim().isEmpty() && !proveedorDTO.getTelefono().matches(PATRON_TELEFONO)) {
            throw new IllegalArgumentException(MENSAJE_TELEFONO_INVALIDO);
        }

        if (proveedorRepository.findByRuc(proveedorDTO.getRuc().trim()).isPresent()) {
            throw new IllegalArgumentException(MENSAJE_DUPLICADO_RUC);
        }
    }
}
