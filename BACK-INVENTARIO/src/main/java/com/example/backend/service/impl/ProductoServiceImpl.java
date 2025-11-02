package com.example.backend.service.impl;

import com.example.backend.entity.Producto;
import com.example.backend.entity.Proveedor;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;



    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Producto> obtenerProductosActivados() {
        return productoRepository.findByEstadoIsTrue();
    }

    @Override
    public List<Producto> obtenerProductosDesactivados() {
        return productoRepository.findByEstadoIsFalse();
    }

    @Override
    public boolean activarProducto(Long id) {
        return cambiarEstadoProducto(id, true);
    }

    @Override
    public boolean desactivarProducto(Long id) {
        return cambiarEstadoProducto(id, false);
    }

    private boolean cambiarEstadoProducto(Long id, boolean activo) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setEstado(activo);
                    productoRepository.save(producto);
                    return true;
                })
                .orElse(false);
    }


    @Override
    public Producto actualizarProducto(Long id, String nombre, String precio, String descripcion,
                                       int stock, String ubicacion, Long proveedorId) {
        // 🧩 Constraint 1: Validar ID del producto
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser un número positivo.");
        }

        // 🧩 Constraint 2: Validar datos obligatorios
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        if (precio == null || precio.trim().isEmpty()) {
            throw new IllegalArgumentException("El precio del producto es obligatorio.");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        // 🧩 Buscar producto
        Producto producto = obtenerProductoPorId(id);
        if (producto == null) {
            throw new EntityNotFoundException("Producto con ID " + id + " no encontrado.");
        }

        // 🧩 Buscar proveedor
        Proveedor proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor con ID " + proveedorId + " no encontrado."));

        // 🧩 Constraint 3: Validar longitud máxima
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar los 100 caracteres.");
        }

        if (descripcion != null && descripcion.length() > 255) {
            throw new IllegalArgumentException("La descripción no puede superar los 255 caracteres.");
        }

        if (ubicacion != null && ubicacion.length() > 100) {
            throw new IllegalArgumentException("La ubicación no puede superar los 100 caracteres.");
        }

        // 🧩 Actualizar atributos válidos
        producto.setNombre(nombre.trim());
        producto.setPrecio(precio.trim());
        producto.setDescripcion(descripcion != null ? descripcion.trim() : null);
        producto.setStock(stock);
        producto.setUbicacion(ubicacion != null ? ubicacion.trim() : null);
        producto.setProveedor(proveedor);

        // 🧩 Guardar producto actualizado
        return productoRepository.save(producto);
    }


    @Override
    public Producto agregarProducto(String nombre, String precio, String descripcion,
                                    int stock, String ubicacion, Boolean estado, Long proveedorId) {

        // 🧩 Constraint 1: Validar campos obligatorios
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        if (precio == null || precio.trim().isEmpty()) {
            throw new IllegalArgumentException("El precio del producto es obligatorio.");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        if (proveedorId == null || proveedorId <= 0) {
            throw new IllegalArgumentException("Debe especificar un ID de proveedor válido.");
        }

        // 🧩 Constraint 2: Validar longitudes máximas
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar los 100 caracteres.");
        }

        if (descripcion != null && descripcion.length() > 255) {
            throw new IllegalArgumentException("La descripción no puede superar los 255 caracteres.");
        }

        if (ubicacion != null && ubicacion.length() > 100) {
            throw new IllegalArgumentException("La ubicación no puede superar los 100 caracteres.");
        }

        // 🧩 Constraint 3: Asignar estado por defecto
        if (estado == null) {
            estado = true;
        }

        // 🧩 Constraint 4: Validar existencia del proveedor
        Proveedor proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor con ID " + proveedorId + " no encontrado."));

        // 🧩 Crear y guardar el nuevo producto
        Producto producto = new Producto(
                nombre.trim(),
                precio.trim(),
                descripcion != null ? descripcion.trim() : null,
                ubicacion != null ? ubicacion.trim() : null,
                stock,
                estado,
                proveedor
        );

        return productoRepository.save(producto);
    }


}
