package com.example.backend.service.impl;

import com.example.backend.dto.request.SalidasRequest;
import com.example.backend.entity.DetalleSalida;
import com.example.backend.entity.Producto;
import com.example.backend.entity.Salidas;
import com.example.backend.entity.Usuario;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.SalidaMapper;
import com.example.backend.repository.Detalle_SalidaRepository;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.repository.SalidaRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.SalidaService;

import com.example.backend.validators.SalidaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalidaServiceImpl implements SalidaService {

    private final Detalle_SalidaRepository detalleSalidaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalidaRepository salidaRepository;

    private final SalidaMapper salidaMapper;
    private final SalidaValidator salidaValidator;

    @Override
    public List<DetalleSalida> crearDetalleSalida(List<SalidasRequest> detalles) {

        salidaValidator.validarLista(detalles);

        Usuario usuario = obtenerUsuario(detalles.get(0).getUsuario());

        Salidas salida = salidaMapper.toSalida(detalles.get(0), usuario);

        salida = salidaRepository.save(salida);

        List<DetalleSalida> detalleSalidas = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (SalidasRequest dto : detalles) {

            Producto producto = obtenerProducto(dto.getProducto());

            salidaValidator.validarStock(producto, dto.getCantidad());

            DetalleSalida detalle = salidaMapper.toDetalle(dto, producto, usuario, salida);

            actualizarStock(producto, detalle.getStockActual());

            total = total.add(detalle.getSubtotal());

            detalleSalidas.add(detalle);
        }

        detalleSalidaRepository.saveAll(detalleSalidas);

        salida.setTotal(total);
        salidaRepository.save(salida);
        return detalleSalidas;
    }

    private Usuario obtenerUsuario(String username) {

        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );
    }

    private Producto obtenerProducto(String nombre) {

        return productoRepository.findByNombre(nombre)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado: " + nombre)
                );
    }

    private void actualizarStock(Producto producto, int nuevoStock) {
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }

    @Override
    public DetalleSalida obtenerPorId(Long id) {

        return detalleSalidaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salida no encontrada")
                );
    }

    @Override
    public List<DetalleSalida> obtenerTodas() {
        return detalleSalidaRepository.findAll();
    }
}