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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetalleEntradaService {

    private final Detalle_EntradaRepository detalle_EntradaRepository;
    private final EntradaRepository entradaRepository;
    private final ProductoRepository productoRepository;



    public List<DetalleEntrada> crearDetalleEntrada(List<DetalleEntrada> listaDetalleEntrada) {
        if (listaDetalleEntrada == null || listaDetalleEntrada.isEmpty()) {
            throw new IllegalArgumentException("La lista de detalles de entrada no puede estar vacía");
        }
        List<DetalleEntrada> guardados = new ArrayList<>();

        for (DetalleEntrada detalle : listaDetalleEntrada) {
            if (detalle.getEntrada() == null || detalle.getEntrada().getFechaEntrada() == null) {
                throw new IllegalArgumentException("Cada detalle debe incluir una fecha de entrada válida");
            }
            // Buscar entrada existente por fecha
            Optional<Entradas> entradaExistenteOpt = entradaRepository.findByFechaEntrada(detalle.getEntrada().getFechaEntrada());
            Entradas entradaGuardada;

            if (entradaExistenteOpt.isPresent()) {
                entradaGuardada = entradaExistenteOpt.get();
            } else {
                Entradas nuevaEntrada = new Entradas();
                nuevaEntrada.setFechaEntrada(detalle.getEntrada().getFechaEntrada());
                entradaGuardada = entradaRepository.save(nuevaEntrada);
            }

            detalle.setEntrada(entradaGuardada);

            // 🧩 Validar y actualizar stock del producto
            Producto producto = productoRepository.findById(detalle.getProducto().getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalle.getProducto().getProductoId()));

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero para el producto: " + producto.getNombre());
            }


            int nuevoStock = producto.getStock() + detalle.getCantidad();
            producto.setStock(nuevoStock);
            productoRepository.save(producto);

            // Guardar detalle
            guardados.add(detalle_EntradaRepository.save(detalle));
        }

        return guardados;
    }


    public DetalleEntrada obtenerPorId(Long id) {
        return detalle_EntradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
    }


    public List<DetalleEntrada> obtenerTodos() {
        return detalle_EntradaRepository.findAll();
    }


    public DetalleEntrada actualizarDetalleEntrada(Long detalleEntradaId, DetalleEntrada detalleEntrada) {
        DetalleEntrada existente = detalle_EntradaRepository.findById(detalleEntradaId)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        int cantidadAntigua = existente.getCantidad();
        int cantidadNueva = detalleEntrada.getCantidad();

        Producto producto = existente.getProducto();
        if (producto == null) throw new RuntimeException("Producto no encontrado");

        int diferencia = cantidadNueva - cantidadAntigua;
        producto.setStock(producto.getStock() + diferencia);
        productoRepository.save(producto);

        existente.setCantidad(detalleEntrada.getCantidad());
        existente.setDescripcion(detalleEntrada.getDescripcion());

        return detalle_EntradaRepository.save(existente);
    }
}