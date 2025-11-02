package com.example.backend.service;

import com.example.backend.entity.DetalleEntrada;

import com.example.backend.entity.Entradas;
import com.example.backend.entity.Producto;
import com.example.backend.repository.Detalle_EntradaRepository;
import com.example.backend.repository.EntradaRepository;
import com.example.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleEntradaService {

    private final Detalle_EntradaRepository detalle_EntradaRepository;
    private final EntradaRepository entradaRepository;
    private final ProductoRepository productoRepository;

    // ------------------ CONSTANTES ------------------
    private static final String ERROR_LISTA_VACIA = "La lista de detalles de entrada no puede estar vacía";
    private static final String ERROR_FECHA_INVALIDA = "Cada detalle debe incluir una fecha de entrada válida";
    private static final String ERROR_PRODUCTO_NO_ENCONTRADO = "Producto no encontrado con ID: %d";
    private static final String ERROR_CANTIDAD_INVALIDA = "La cantidad debe ser mayor a cero para el producto: %s";
    private static final String ERROR_DETALLE_NO_ENCONTRADO = "Detalle no encontrado";
    private static final String ERROR_PRODUCTO_NULL = "Producto no encontrado";

    // ------------------ CREAR DETALLES ------------------
    public List<DetalleEntrada> crearDetalleEntrada(List<DetalleEntrada> listaDetalleEntrada) {
        if (listaDetalleEntrada == null || listaDetalleEntrada.isEmpty()) {
            throw new IllegalArgumentException(ERROR_LISTA_VACIA);
        }

        List<DetalleEntrada> guardados = new ArrayList<>();

        for (DetalleEntrada detalle : listaDetalleEntrada) {
            validarDetalleEntrada(detalle);

            Entradas entradaGuardada = obtenerOCrearEntrada(detalle.getEntrada().getFechaEntrada());
            detalle.setEntrada(entradaGuardada);

            Producto producto = obtenerProducto(detalle.getProducto().getProductoId());

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(String.format(ERROR_CANTIDAD_INVALIDA, producto.getNombre()));
            }

            // Actualizar stock del producto
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);

            // Guardar detalle
            guardados.add(detalle_EntradaRepository.save(detalle));
        }

        return guardados;
    }

    // ------------------ OBTENER POR ID ------------------
    public DetalleEntrada obtenerPorId(Long id) {
        return detalle_EntradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ERROR_DETALLE_NO_ENCONTRADO));
    }

    // ------------------ OBTENER TODOS ------------------
    public List<DetalleEntrada> obtenerTodos() {
        return detalle_EntradaRepository.findAll();
    }

    // ------------------ ACTUALIZAR DETALLE ------------------
    public DetalleEntrada actualizarDetalleEntrada(Long detalleEntradaId, DetalleEntrada detalleEntrada) {
        DetalleEntrada existente = detalle_EntradaRepository.findById(detalleEntradaId)
                .orElseThrow(() -> new RuntimeException(ERROR_DETALLE_NO_ENCONTRADO));

        Producto producto = existente.getProducto();
        if (producto == null) throw new RuntimeException(ERROR_PRODUCTO_NULL);

        // Ajustar stock según diferencia
        int diferencia = detalleEntrada.getCantidad() - existente.getCantidad();
        producto.setStock(producto.getStock() + diferencia);
        productoRepository.save(producto);

        // Actualizar detalle
        existente.setCantidad(detalleEntrada.getCantidad());
        existente.setDescripcion(detalleEntrada.getDescripcion());

        return detalle_EntradaRepository.save(existente);
    }

    // ------------------ MÉTODOS PRIVADOS ------------------
    private void validarDetalleEntrada(DetalleEntrada detalle) {
        if (detalle.getEntrada() == null || detalle.getEntrada().getFechaEntrada() == null) {
            throw new IllegalArgumentException(ERROR_FECHA_INVALIDA);
        }
        if (detalle.getProducto() == null || detalle.getProducto().getProductoId() == null) {
            throw new IllegalArgumentException("Detalle debe contener un producto válido");
        }
    }

    private Entradas obtenerOCrearEntrada(Date fechaEntrada) {
        return entradaRepository.findByFechaEntrada(fechaEntrada)
                .orElseGet(() -> {
                    Entradas nuevaEntrada = new Entradas();
                    nuevaEntrada.setFechaEntrada(fechaEntrada);
                    return entradaRepository.save(nuevaEntrada);
                });
    }

    private Producto obtenerProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException(String.format(ERROR_PRODUCTO_NO_ENCONTRADO, productoId)));
    }
}
