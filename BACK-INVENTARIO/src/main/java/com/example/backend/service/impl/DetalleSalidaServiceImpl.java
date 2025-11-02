package com.example.backend.service.impl;

import com.example.backend.entity.Detalle_Salida;
import com.example.backend.entity.Producto;
import com.example.backend.entity.Salidas;
import com.example.backend.repository.Detalle_SalidaRepository;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.repository.SalidaRepository;
import com.example.backend.service.DetalleSalidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DetalleSalidaServiceImpl implements DetalleSalidaService {

    private final Detalle_SalidaRepository detalle_SalidaRepository;
    private  final ProductoRepository   productoRepository;
    private final SalidaRepository salidaRepository;


    @Override
    public Detalle_Salida obtenerPorId(Long id) {
        return detalle_SalidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    @Override
    public List<Detalle_Salida> obtenerTodas() {
        return detalle_SalidaRepository.findAll();
    }

    @Override
    public Map<String, Boolean> actualizarDetalleSalida(Long detalleSalidaId, Detalle_Salida detalleEntrada) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", false);

        Optional<Detalle_Salida> detalleExistenteOpt = detalle_SalidaRepository.findById(detalleSalidaId);
        if (detalleExistenteOpt.isEmpty()) {
            return response; // No existe el detalle
        }

        Detalle_Salida detalleExistente = detalleExistenteOpt.get();

        // Ajuste del stock si cambia la cantidad
        int cantidadAntigua = detalleExistente.getCantidad();
        int cantidadNueva = detalleEntrada.getCantidad();

        if (cantidadNueva != cantidadAntigua) {
            Producto producto = detalleEntrada.getProducto();
            Producto productoActualizado = productoRepository.findById(producto.getProductoId()).orElse(null);

            if (productoActualizado == null) {
                return response; // Producto no encontrado
            }

            int diferenciaCantidad = cantidadNueva - cantidadAntigua;
            int nuevoStock = productoActualizado.getStock() - diferenciaCantidad;
            productoActualizado.setStock(nuevoStock);
            productoRepository.save(productoActualizado);
        }

        // Actualizar los campos del detalle
        detalleExistente.setProducto(detalleEntrada.getProducto());
        detalleExistente.setDescripcion(detalleEntrada.getDescripcion());
        detalleExistente.setCantidad(detalleEntrada.getCantidad());

        detalle_SalidaRepository.save(detalleExistente);
        response.put("success", true);

        return response;
    }

    @Override
    public List<Detalle_Salida> crearDetalleSalida(List<Detalle_Salida> listaDetalleSalida) {
        List<Detalle_Salida> guardados = new ArrayList<>();
        if (listaDetalleSalida == null || listaDetalleSalida.isEmpty()) {
            throw new IllegalArgumentException("La lista de detalles de salida no puede estar vacía");
        }


        for (Detalle_Salida detalle : listaDetalleSalida) {
            if (detalle.getSalida() == null || detalle.getSalida().getFechaSalida() == null) {
                throw new IllegalArgumentException("Cada detalle debe tener una fecha de salida válida");
            }
            // Buscar salida existente por fecha
            Optional<Salidas> salidaExistenteOpt = salidaRepository.findByFechaSalida(detalle.getSalida().getFechaSalida());
            Salidas salidaGuardada;

            if (salidaExistenteOpt.isPresent()) {
                salidaGuardada = salidaExistenteOpt.get();
            } else {
                Salidas nuevaSalida = new Salidas();
                nuevaSalida.setFechaSalida(detalle.getSalida().getFechaSalida());
                salidaGuardada = salidaRepository.save(nuevaSalida);
            }

            detalle.setSalida(salidaGuardada);

            // 🧮 Validar y actualizar el stock del producto
            Producto producto = productoRepository.findById(detalle.getProducto().getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalle.getProducto().getProductoId()));

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero para el producto: " + producto.getNombre());
            }

            if (producto.getStock() < detalle.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            int nuevoStock = producto.getStock() - detalle.getCantidad();
            producto.setStock(nuevoStock);
            productoRepository.save(producto);

            // Guardar detalle
            guardados.add(detalle_SalidaRepository.save(detalle));
        }

        return guardados;
    }
}
