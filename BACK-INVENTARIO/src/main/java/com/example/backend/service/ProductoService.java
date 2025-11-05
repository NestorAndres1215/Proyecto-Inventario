package com.example.backend.service;

import com.example.backend.dto.ProductoDTO;
import com.example.backend.entity.Producto;
import com.example.backend.entity.Proveedor;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    public Producto agregarProducto(ProductoDTO productoDTO) {

        validarDatosProducto(productoDTO);

        Proveedor proveedor = proveedorRepository.findById(productoDTO.getProveedorId())
                .orElseThrow(() -> new EntityNotFoundException(MENSAJE_PROVEEDOR_NO_ENCONTRADO));

        Producto producto = Producto.builder()
                .nombre(productoDTO.getNombre())
                .precio(productoDTO.getPrecio())
                .descripcion(productoDTO.getDescripcion())
                .ubicacion(productoDTO.getUbicacion())
                .stock(productoDTO.getStock())
                .estado(true)
                .proveedor(proveedor)
                .build();

        return productoRepository.save(producto);
    }

    // ------------------ ACTUALIZAR ------------------
    public Producto actualizarProducto(ProductoDTO productoDTO) {

        Producto producto = productoRepository.findById(productoDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(MENSAJE_PRODUCTO_NO_ENCONTRADO));

        validarDatosProducto(productoDTO);

        Proveedor proveedor = proveedorRepository.findById(productoDTO.getProveedorId())
                .orElseThrow(() -> new EntityNotFoundException(MENSAJE_PROVEEDOR_NO_ENCONTRADO));

        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setUbicacion(productoDTO.getUbicacion());
        producto.setStock(productoDTO.getStock());
        producto.setProveedor(proveedor);

        return productoRepository.save(producto);
    }

    // ------------------ VALIDACIONES ------------------
    private void validarDatosProducto(ProductoDTO productoDTO) {
        if (productoDTO.getNombre() == null || productoDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_NOMBRE_OBLIGATORIO);
        }
        if (productoDTO.getPrecio() == null || productoDTO.getPrecio().trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_PRECIO_OBLIGATORIO);
        }
        if (productoDTO.getStock() < 0) {
            throw new IllegalArgumentException(MENSAJE_STOCK_NEGATIVO);
        }
        if (productoDTO.getNombre().length() > MAX_NOMBRE) {
            throw new IllegalArgumentException(MENSAJE_MAX_100);
        }
        if (productoDTO.getDescripcion() != null && productoDTO.getDescripcion().length() > MAX_DESCRIPCION) {
            throw new IllegalArgumentException(MENSAJE_MAX_255);
        }
        if (productoDTO.getUbicacion() != null && productoDTO.getUbicacion().length() > MAX_UBICACION) {
            throw new IllegalArgumentException(MENSAJE_MAX_100);
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
