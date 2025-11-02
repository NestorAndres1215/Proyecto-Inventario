package com.example.backend.service;

import com.example.backend.entity.DetalleSalida;

import com.example.backend.entity.Producto;
import com.example.backend.entity.Salidas;
import com.example.backend.repository.Detalle_SalidaRepository;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.repository.SalidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@RequiredArgsConstructor
public class DetalleSalidaService {

    private final Detalle_SalidaRepository detalle_SalidaRepository;
    private final ProductoRepository productoRepository;
    private final SalidaRepository salidaRepository;

    // ------------------ CONSTANTES ------------------
    private static final String ERROR_LISTA_VACIA = "La lista de detalles de salida no puede estar vacía";
    private static final String ERROR_FECHA_INVALIDA = "Cada detalle debe tener una fecha de salida válida";
    private static final String ERROR_PRODUCTO_NO_ENCONTRADO = "Producto no encontrado con ID: %d";
    private static final String ERROR_CANTIDAD_INVALIDA = "La cantidad debe ser mayor a cero para el producto: %s";
    private static final String ERROR_STOCK_INSUFICIENTE = "Stock insuficiente para el producto: %s";
    private static final String ERROR_DETALLE_NO_ENCONTRADO = "Detalle no encontrado";

    // ------------------ CREAR DETALLES ------------------
    public List<DetalleSalida> crearDetalleSalida(List<DetalleSalida> listaDetalleSalida) {
        if (listaDetalleSalida == null || listaDetalleSalida.isEmpty()) {
            throw new IllegalArgumentException(ERROR_LISTA_VACIA);
        }

        List<DetalleSalida> guardados = new ArrayList<>();

        for (DetalleSalida detalle : listaDetalleSalida) {
            validarDetalleSalida(detalle);

            Salidas salidaGuardada = obtenerOCrearSalida(detalle.getSalida().getFechaSalida());
            detalle.setSalida(salidaGuardada);

            Producto producto = obtenerProducto(detalle.getProducto().getProductoId());

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(String.format(ERROR_CANTIDAD_INVALIDA, producto.getNombre()));
            }

            if (producto.getStock() < detalle.getCantidad()) {
                throw new IllegalStateException(String.format(ERROR_STOCK_INSUFICIENTE, producto.getNombre()));
            }

            // Actualizar stock
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            // Guardar detalle
            guardados.add(detalle_SalidaRepository.save(detalle));
        }

        return guardados;
    }

    // ------------------ OBTENER ------------------
    public DetalleSalida obtenerPorId(Long id) {
        return detalle_SalidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ERROR_DETALLE_NO_ENCONTRADO));
    }

    public List<DetalleSalida> obtenerTodas() {
        return detalle_SalidaRepository.findAll();
    }

    // ------------------ ACTUALIZAR ------------------
    public Map<String, Boolean> actualizarDetalleSalida(Long detalleSalidaId, DetalleSalida detalleSalida) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", false);

        DetalleSalida detalleExistente = detalle_SalidaRepository.findById(detalleSalidaId)
                .orElse(null);

        if (detalleExistente == null) {
            return response; // Detalle no encontrado
        }

        Producto productoExistente = detalleExistente.getProducto();
        Producto productoNuevo = obtenerProducto(detalleSalida.getProducto().getProductoId());

        // Ajuste de stock
        int diferencia = detalleSalida.getCantidad() - detalleExistente.getCantidad();
        if (diferencia != 0) {
            if (productoNuevo.getStock() < diferencia) {
                throw new IllegalStateException(String.format(ERROR_STOCK_INSUFICIENTE, productoNuevo.getNombre()));
            }
            productoNuevo.setStock(productoNuevo.getStock() - diferencia);
            productoRepository.save(productoNuevo);
        }

        // Actualizar detalle
        detalleExistente.setProducto(productoNuevo);
        detalleExistente.setDescripcion(detalleSalida.getDescripcion());
        detalleExistente.setCantidad(detalleSalida.getCantidad());
        detalle_SalidaRepository.save(detalleExistente);

        response.put("success", true);
        return response;
    }

    // ------------------ MÉTODOS PRIVADOS ------------------
    private void validarDetalleSalida(DetalleSalida detalle) {
        if (detalle.getSalida() == null || detalle.getSalida().getFechaSalida() == null) {
            throw new IllegalArgumentException(ERROR_FECHA_INVALIDA);
        }
        if (detalle.getProducto() == null || detalle.getProducto().getProductoId() == null) {
            throw new IllegalArgumentException("Detalle debe contener un producto válido");
        }
    }

    private Salidas obtenerOCrearSalida(Date fechaSalida) {
        return salidaRepository.findByFechaSalida(fechaSalida)
                .orElseGet(() -> {
                    Salidas nuevaSalida = new Salidas();
                    nuevaSalida.setFechaSalida(fechaSalida);
                    return salidaRepository.save(nuevaSalida);
                });
    }

    private Producto obtenerProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException(String.format(ERROR_PRODUCTO_NO_ENCONTRADO, productoId)));
    }
}
