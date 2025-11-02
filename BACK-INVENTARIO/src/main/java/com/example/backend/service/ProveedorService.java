package com.example.backend.service;

import com.example.backend.entity.Proveedor;
import com.example.backend.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class  ProveedorService {

    private  final ProveedorRepository proveedorRepository;



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


    public Proveedor crearProveedor(String nombre, String ruc, String direccion,
                                    String telefono, String email, Boolean estado) {

        // 🧩 Constraint 1: Validar campos obligatorios
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proveedor es obligatorio.");
        }

        if (ruc == null || ruc.trim().isEmpty()) {
            throw new IllegalArgumentException("El RUC del proveedor es obligatorio.");
        }

        // 🧩 Constraint 2: Validar formato del RUC (por ejemplo, Perú: 11 dígitos)
        if (!ruc.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe contener exactamente 11 dígitos numéricos.");
        }

        // 🧩 Constraint 3: Validar email si se proporciona
        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException("El formato del correo electrónico no es válido.");
            }
        }

        // 🧩 Constraint 4: Validar teléfono (opcional)
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (!telefono.matches("^\\d{7,15}$")) {
                throw new IllegalArgumentException("El número de teléfono debe tener entre 7 y 15 dígitos.");
            }
        }

        // 🧩 Constraint 5: Validar duplicado (RUC único)
        if (proveedorRepository.findByRuc(ruc).isPresent()) {
            throw new IllegalArgumentException("Ya existe un proveedor con el mismo RUC.");
        }

        // 🧩 Estado por defecto
        if (estado == null) {
            estado = true;
        }

        // 🧩 Crear entidad limpia
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(nombre.trim());
        proveedor.setRuc(ruc.trim());
        proveedor.setDireccion(direccion != null ? direccion.trim() : null);
        proveedor.setTelefono(telefono != null ? telefono.trim() : null);
        proveedor.setEmail(email != null ? email.trim() : null);
        proveedor.setEstado(estado);

        // 🧩 Guardar en base de datos
        return proveedorRepository.save(proveedor);
    }



    public Proveedor actualizarProveedor(Long id, String nombre, String ruc,
                                         String direccion, String telefono, String email) {
        // 🔍 Buscar proveedor existente
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor con ID " + id + " no encontrado."));

        // 🧾 Validaciones de negocio
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío.");
        }

        if (ruc == null || !ruc.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe contener 11 dígitos numéricos válidos.");
        }

        if (email != null && !email.isEmpty() && !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido.");
        }

        // 🚫 Evitar duplicados de RUC (si el RUC cambió)
        proveedorRepository.findByRuc(ruc).ifPresent(existing -> {
            if (!existing.getProveedorId().equals(id)) {
                throw new IllegalStateException("Ya existe un proveedor registrado con el RUC: " + ruc);
            }
        });

        // ✏️ Actualizar datos
        proveedor.setNombre(nombre.trim());
        proveedor.setRuc(ruc.trim());
        proveedor.setDireccion(direccion != null ? direccion.trim() : null);
        proveedor.setTelefono(telefono != null ? telefono.trim() : null);
        proveedor.setEmail(email != null ? email.trim() : null);

        // 💾 Guardar cambios
        return proveedorRepository.save(proveedor);
    }



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


}
