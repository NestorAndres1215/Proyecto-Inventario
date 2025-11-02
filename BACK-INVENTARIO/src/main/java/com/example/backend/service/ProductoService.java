package com.example.backend.service;

import com.example.backend.entity.Producto;
import com.example.backend.entity.Proveedor;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    // ------------------ CONSTANTES ------------------
    private static final String MENSAJE_NOMBRE_OBLIGATORIO = "El nombre del producto es obligatorio.";
    private static final String MENSAJE_PRECIO_OBLIGATORIO = "El precio del producto es obligatorio.";
    private static final String MENSAJE_STOCK_NEGATIVO = "El stock no puede ser negativo.";
    private static final String MENSAJE_PROVEEDOR_NO_ENCONTRADO = "Proveedor con ID %d no encontrado.";
    private static final String MENSAJE_PRODUCTO_NO_ENCONTRADO = "Producto con ID %d no encontrado.";
    private static final String MENSAJE_MAX_100 = "El campo %s no puede superar los 100 caracteres.";
    private static final String MENSAJE_MAX_255 = "El campo %s no puede superar los 255 caracteres.";

    private static final int MAX_NOMBRE = 100;
    private static final int MAX_UBICACION = 100;
    private static final int MAX_DESCRIPCION = 255;

    // ------------------ LISTAR ------------------
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerProductosActivados() {
        return productoRepository.findByEstadoIsTrue();
    }

    public List<Producto> obtenerProductosDesactivados() {
        return productoRepository.findByEstadoIsFalse();
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // ------------------ ACTIVAR / DESACTIVAR ------------------
    public boolean activarProducto(Long id) {
        return cambiarEstadoProducto(id, true);
    }

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

    // ------------------ CREAR ------------------
    public Producto agregarProducto(String nombre, String precio, String descripcion,
                                    int stock, String ubicacion, Boolean estado, Long proveedorId) {

        validarDatosProducto(nombre, precio, stock, descripcion, ubicacion);

        if (estado == null) {
            estado = true;
        }

        Proveedor proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MENSAJE_PROVEEDOR_NO_ENCONTRADO, proveedorId)));

        Producto producto = Producto.builder()
                .nombre(nombre.trim())
                .precio(precio.trim())
                .descripcion(descripcion != null ? descripcion.trim() : null)
                .ubicacion(ubicacion != null ? ubicacion.trim() : null)
                .stock(stock)
                .estado(estado)
                .proveedor(proveedor)
                .build();

        return productoRepository.save(producto);
    }

    // ------------------ ACTUALIZAR ------------------
    public Producto actualizarProducto(Long id, String nombre, String precio, String descripcion,
                                       int stock, String ubicacion, Long proveedorId) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser un número positivo.");
        }

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MENSAJE_PRODUCTO_NO_ENCONTRADO, id)));

        validarDatosProducto(nombre, precio, stock, descripcion, ubicacion);

        Proveedor proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MENSAJE_PROVEEDOR_NO_ENCONTRADO, proveedorId)));

        producto.setNombre(nombre.trim());
        producto.setPrecio(precio.trim());
        producto.setDescripcion(descripcion != null ? descripcion.trim() : null);
        producto.setUbicacion(ubicacion != null ? ubicacion.trim() : null);
        producto.setStock(stock);
        producto.setProveedor(proveedor);

        return productoRepository.save(producto);
    }

    // ------------------ VALIDACIONES ------------------
    private void validarDatosProducto(String nombre, String precio, int stock,
                                      String descripcion, String ubicacion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_NOMBRE_OBLIGATORIO);
        }
        if (precio == null || precio.trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_PRECIO_OBLIGATORIO);
        }
        if (stock < 0) {
            throw new IllegalArgumentException(MENSAJE_STOCK_NEGATIVO);
        }
        if (nombre.length() > MAX_NOMBRE) {
            throw new IllegalArgumentException(String.format(MENSAJE_MAX_100, "nombre"));
        }
        if (descripcion != null && descripcion.length() > MAX_DESCRIPCION) {
            throw new IllegalArgumentException(String.format(MENSAJE_MAX_255, "descripción"));
        }
        if (ubicacion != null && ubicacion.length() > MAX_UBICACION) {
            throw new IllegalArgumentException(String.format(MENSAJE_MAX_100, "ubicación"));
        }
    }

    // ------------------ STOCK ------------------
    public Producto productoConMayorStock() {
        return productoRepository.findTopByStockDesc()
                .stream().findFirst().orElse(null);
    }

    public Producto productoConMenorStock() {
        return productoRepository.findTopByStockAsc()
                .stream().findFirst().orElse(null);
    }

    // ------------------ POR PROVEEDOR ------------------
    public List<Producto> listarProductosPorProveedor(Long proveedorId) {
        return productoRepository.findByProveedorProveedorId(proveedorId);
    }

    // ------------------ TOP 10 MÁS BARATOS ------------------
    public List<Producto> top10ProductosMasBaratos() {
        return productoRepository.findTop10ByOrderByPrecioAsc();
    }

    public List<Producto> top10ProductosMasBaratosActivos() {
        return productoRepository.findTop10ByEstadoIsTrueOrderByPrecioAsc();
    }

}
