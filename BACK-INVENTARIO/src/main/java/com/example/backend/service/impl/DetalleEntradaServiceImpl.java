package com.example.backend.service.impl;

import com.example.backend.constants.NotFoundMessages;
import com.example.backend.entity.DetalleEntrada;
import com.example.backend.entity.Entradas;
import com.example.backend.entity.Producto;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;

import com.example.backend.repository.Detalle_EntradaRepository;
import com.example.backend.repository.EntradaRepository;
import com.example.backend.repository.ProductoRepository;

import com.example.backend.service.DetalleEntradaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DetalleEntradaServiceImpl implements DetalleEntradaService {

    private final Detalle_EntradaRepository detalleEntradaRepository;
    private final EntradaRepository entradaRepository;
    private final ProductoRepository productoRepository;

    private static final String ERROR_LISTA_VACIA = "La lista de detalles de entrada no puede estar vacía";
    private static final String ERROR_FECHA_INVALIDA = "Cada detalle debe incluir una fecha de entrada válida";
    private static final String ERROR_PRODUCTO_INVALIDO = "Cada detalle debe contener un producto válido";
    private static final String ERROR_CANTIDAD = "La cantidad debe ser mayor a cero.";

    @Override
    public List<DetalleEntrada> crearDetalleEntrada(List<DetalleEntrada> detalles) {

        validarLista(detalles);

        List<DetalleEntrada> guardados = new ArrayList<>();

        for (DetalleEntrada detalle : detalles) {

            validarDetalle(detalle);

            Entradas entrada = obtenerOCrearEntrada(detalle.getEntrada().getFechaEntrada());

            Producto producto = obtenerProducto(detalle.getProducto().getProductoId());

            validarCantidad(detalle.getCantidad());

            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);

            detalle.setEntrada(entrada);
            detalle.setProducto(producto);

            guardados.add(detalleEntradaRepository.save(detalle));
        }

        return guardados;
    }

    @Override
    public DetalleEntrada obtenerPorId(Long id) {
        return detalleEntradaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.ENTRADA_NO_ENCONTRADO));
    }

    @Override
    public List<DetalleEntrada> obtenerTodos() {
        return detalleEntradaRepository.findAll();
    }

    @Override
    public DetalleEntrada actualizarDetalleEntrada(Long id, DetalleEntrada dto) {

        DetalleEntrada detalle = obtenerPorId(id);

        Producto producto = detalle.getProducto();

        int diferencia = dto.getCantidad() - detalle.getCantidad();

        producto.setStock(producto.getStock() + diferencia);
        productoRepository.save(producto);

        detalle.setCantidad(dto.getCantidad());
        detalle.setDescripcion(dto.getDescripcion());

        return detalleEntradaRepository.save(detalle);
    }

    private void validarLista(List<DetalleEntrada> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException(ERROR_LISTA_VACIA);
        }
    }

    private void validarDetalle(DetalleEntrada detalle) {

        if (detalle.getEntrada() == null || detalle.getEntrada().getFechaEntrada() == null) {
            throw new BadRequestException(ERROR_FECHA_INVALIDA);
        }

        if (detalle.getProducto() == null || detalle.getProducto().getProductoId() == null) {
            throw new BadRequestException(ERROR_PRODUCTO_INVALIDO);
        }
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new BadRequestException(ERROR_CANTIDAD);
        }
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.PRODUCTO_NO_ENCONTRADO));
    }

    private Entradas obtenerOCrearEntrada(Date fecha) {

        return entradaRepository.findByFechaEntrada(fecha)
                .orElseGet(() -> {
                    Entradas entrada = new Entradas();
                    entrada.setFechaEntrada(fecha);
                    return entradaRepository.save(entrada);
                });
    }
}