package com.example.backend.service.impl;

import com.example.backend.entity.Detalle_Entrada;
import com.example.backend.entity.Entradas;
import com.example.backend.entity.Producto;
import com.example.backend.repository.Detalle_EntradaRepository;
import com.example.backend.repository.EntradaRepository;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.service.DetalleEntradaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleEntradaServiceImpl implements DetalleEntradaService {

    private final Detalle_EntradaRepository detalle_EntradaRepository;
    private final EntradaRepository entradaRepository;
    private final ProductoRepository productoRepository;

    public DetalleEntradaServiceImpl(Detalle_EntradaRepository detalle_EntradaRepository,
                                     EntradaRepository entradaRepository,
                                     ProductoRepository productoRepository) {
        this.detalle_EntradaRepository = detalle_EntradaRepository;
        this.entradaRepository = entradaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Detalle_Entrada> crearDetalleEntrada(List<Detalle_Entrada> listaDetalleEntrada) {
        List<Detalle_Entrada> guardados = new ArrayList<>();

        for (Detalle_Entrada detalle : listaDetalleEntrada) {
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

            // Actualizar stock del producto
            Producto producto = productoRepository.findById(detalle.getProducto().getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            int nuevoStock = producto.getStock() + detalle.getCantidad();
            producto.setStock(nuevoStock);
            productoRepository.save(producto);

            // Guardar detalle
            guardados.add(detalle_EntradaRepository.save(detalle));
        }

        return guardados;
    }

    @Override
    public Detalle_Entrada obtenerPorId(Long id) {
        return detalle_EntradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
    }

    @Override
    public List<Detalle_Entrada> obtenerTodos() {
        return detalle_EntradaRepository.findAll();
    }

    @Override
    public Detalle_Entrada actualizarDetalleEntrada(Long detalleEntradaId, Detalle_Entrada detalleEntrada) {
        Detalle_Entrada existente = detalle_EntradaRepository.findById(detalleEntradaId)
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