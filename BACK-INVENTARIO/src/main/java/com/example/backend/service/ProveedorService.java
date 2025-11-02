package com.example.backend.service;

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
    public Proveedor crearProveedor(String nombre, String ruc, String direccion,
                                    String telefono, String email, Boolean estado) {

        validarDatosProveedor(nombre, ruc, telefono, email);

        if (estado == null) {
            estado = true;
        }

        Proveedor proveedor = Proveedor.builder()
                .nombre(nombre.trim())
                .ruc(ruc.trim())
                .direccion(direccion != null ? direccion.trim() : null)
                .telefono(telefono != null ? telefono.trim() : null)
                .email(email != null ? email.trim() : null)
                .estado(estado)
                .build();

        return proveedorRepository.save(proveedor);
    }

    // ------------------ ACTUALIZAR ------------------
    public Proveedor actualizarProveedor(Long id, String nombre, String ruc,
                                         String direccion, String telefono, String email) {

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(MENSAJE_PROVEEDOR_NO_ENCONTRADO, id)));

        validarDatosProveedor(nombre, ruc, telefono, email);

        // Evitar duplicados de RUC si cambia
        proveedorRepository.findByRuc(ruc).ifPresent(existing -> {
            if (!existing.getProveedorId().equals(id)) {
                throw new IllegalStateException(MENSAJE_DUPLICADO_RUC);
            }
        });

        proveedor.setNombre(nombre.trim());
        proveedor.setRuc(ruc.trim());
        proveedor.setDireccion(direccion != null ? direccion.trim() : null);
        proveedor.setTelefono(telefono != null ? telefono.trim() : null);
        proveedor.setEmail(email != null ? email.trim() : null);

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
    private void validarDatosProveedor(String nombre, String ruc, String telefono, String email) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_NOMBRE_OBLIGATORIO);
        }

        if (ruc == null || ruc.trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_RUC_OBLIGATORIO);
        }

        if (!ruc.matches(PATRON_RUC)) {
            throw new IllegalArgumentException(MENSAJE_RUC_INVALIDO);
        }

        if (email != null && !email.trim().isEmpty() && !email.matches(PATRON_EMAIL)) {
            throw new IllegalArgumentException(MENSAJE_EMAIL_INVALIDO);
        }

        if (telefono != null && !telefono.trim().isEmpty() && !telefono.matches(PATRON_TELEFONO)) {
            throw new IllegalArgumentException(MENSAJE_TELEFONO_INVALIDO);
        }

        if (proveedorRepository.findByRuc(ruc.trim()).isPresent()) {
            throw new IllegalArgumentException(MENSAJE_DUPLICADO_RUC);
        }
    }
}
