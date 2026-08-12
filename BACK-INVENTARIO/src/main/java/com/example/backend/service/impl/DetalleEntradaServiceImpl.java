package com.example.backend.service.impl;

import com.example.backend.entity.DetalleEntrada;
import com.example.backend.entity.Entradas;
import com.example.backend.entity.Producto;
import com.example.backend.entity.Usuario;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.DetalleEntradaMapper;
import com.example.backend.repository.Detalle_EntradaRepository;
import com.example.backend.repository.EntradaRepository;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.service.DetalleEntradaService;
import com.example.backend.validators.DetalleEntradaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DetalleEntradaServiceImpl
        implements DetalleEntradaService {

    private final Detalle_EntradaRepository detalleEntradaRepository;
    private final EntradaRepository entradaRepository;
    private final ProductoRepository productoRepository;

    private final DetalleEntradaValidator detalleEntradaValidator;
    private final DetalleEntradaMapper detalleEntradaMapper;

    @Override
    public List<DetalleEntrada> crearDetalleEntrada(List<DetalleEntrada> detalles) {
        detalleEntradaValidator.validarLista(detalles);

        List<DetalleEntrada> guardados = new ArrayList<>();

        for (DetalleEntrada detalle : detalles) {

            detalleEntradaValidator.validarDetalle(detalle);
            detalleEntradaValidator.validarCantidad(detalle.getCantidad());

            Entradas entrada = obtenerOCrearEntrada(
                    detalle.getEntrada().getFechaEntrada(),
                    detalle.getEntrada().getUsuario()
            );

            Producto producto = obtenerProducto(
                    detalle.getProducto().getProductoId()
            );

            detalle.setPrecioUnitario(producto.getPrecio());

            detalle.setSubtotal(
                    producto.getPrecio().multiply(
                            BigDecimal.valueOf(detalle.getCantidad())
                    )
            );

            detalle.setStockAnterior(producto.getStock());

            producto.setStock(
                    producto.getStock() + detalle.getCantidad()
            );

            detalle.setStockActual(producto.getStock());

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
                        new ResourceNotFoundException("Entrada no encontrada")
                );
    }

    @Override
    public List<DetalleEntrada> obtenerTodos() {
        return detalleEntradaRepository.findAll();
    }

    @Override
    public DetalleEntrada actualizarDetalleEntrada(Long id, DetalleEntrada dto) {

        DetalleEntrada detalle = obtenerPorId(id);

        detalleEntradaValidator.validarCantidad(dto.getCantidad());

        Producto producto = detalle.getProducto();
        int diferencia = dto.getCantidad() - detalle.getCantidad();

        producto.setStock(producto.getStock() + diferencia);

        productoRepository.save(producto);

        detalleEntradaMapper.updateEntity(detalle, dto);

        return detalleEntradaRepository.save(detalle);
    }

    private Producto obtenerProducto(Long id) {

        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado")
                );
    }

    private Entradas obtenerOCrearEntrada(
            LocalDate fechaEntrada,
            Usuario usuario
    ) {
        Entradas entrada = new Entradas();
        entrada.setFechaEntrada(fechaEntrada);
        entrada.setFechaRegistro(LocalDate.now());
        entrada.setEstado("Activo");
        entrada.setUsuario(usuario);

        return entradaRepository.save(entrada);
    }
}