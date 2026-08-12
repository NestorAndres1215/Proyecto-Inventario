package com.example.backend.service.impl;

import com.example.backend.dto.request.ProductoRequest;
import com.example.backend.entity.Producto;
import com.example.backend.entity.Proveedor;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.ProductoMapper;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoMapper productoMapper;

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
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
    public Producto obtenerProductoPorId(Long id) {
        return obtenerProducto(id);
    }

    @Override
    public Producto activarProducto(Long id) {
        return cambiarEstadoProducto(id, true);
    }

    @Override
    public Producto desactivarProducto(Long id) {
        return cambiarEstadoProducto(id, false);
    }

    private Producto cambiarEstadoProducto(Long id, boolean estado) {
        Producto producto = obtenerProducto(id);
        producto.setEstado(estado);
        return productoRepository.save(producto);
    }

    @Override
    public Producto agregarProducto(ProductoRequest dto) {
        Proveedor proveedor = obtenerProveedor(dto.getProveedorId());
        Producto producto = productoMapper.toEntity(dto, proveedor);
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarProducto(ProductoRequest dto) {
        Producto producto = obtenerProducto(dto.getId());
        Proveedor proveedor = obtenerProveedor(dto.getProveedorId());
        productoMapper.updateEntity(producto, dto, proveedor);
        return productoRepository.save(producto);
    }

    private Producto obtenerProducto(Long id) {

        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado")
                );
    }

    private Proveedor obtenerProveedor(Long id) {

        return proveedorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Proveedor no encontrado")
                );
    }

    @Override
    public List<Producto> listarProductosPorProveedor(Long proveedorId) {
        return productoRepository.findByProveedorProveedorId(proveedorId);
    }

    @Override
    public List<Producto> top10ProductosMasBaratos() {
        return productoRepository.findTop10ByOrderByPrecioDesc();
    }

    @Override
    public List<Producto> top10ProductosMasCaros() {
        return productoRepository.findTop10ByOrderByPrecioAsc();
    }

    @Override
    public List<Producto> listarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Producto> top10ProductosMasBaratosActivos() {
        return productoRepository.findTop10ByEstadoIsTrueOrderByPrecioDesc();
    }

    @Override
    public List<Producto> top10ProductosMasCarosActivos() {
        return productoRepository.findTop10ByEstadoIsTrueOrderByPrecioAsc();
    }

    @Override
    public Producto productoConMayorStock() {
        return productoRepository.findTopByOrderByStockDesc();
    }

    @Override
    public Producto productoConMenorStock() {
        return productoRepository.findTopByOrderByStockAsc();
    }

    @Override
    public List<Producto> productosConStockBajo(int limite) {
        return productoRepository.findByStockLessThanEqual(limite);
    }

    @Override
    public List<Producto> productosSinStock() {
        return productoRepository.findByStockEquals(0);
    }
}