package com.example.backend.controller;

import com.example.backend.entity.DetalleEntrada;
import com.example.backend.service.DetalleEntradaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/entradas")
@Tag(name = "Entradas")
@RequiredArgsConstructor
public class EntradaController {

    private final DetalleEntradaService detalleEntradaService;

    @PostMapping
    public ResponseEntity<List<DetalleEntrada>> crearEntrada(@RequestBody List<DetalleEntrada> listaDetalleEntrada) {
        return ResponseEntity.ok(detalleEntradaService.crearDetalleEntrada(listaDetalleEntrada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleEntrada> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(detalleEntradaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<DetalleEntrada>> obtenerTodos() {
        return ResponseEntity.ok(detalleEntradaService.obtenerTodos());
    }

    @PutMapping("/{detalleEntradaId}")
    public ResponseEntity<DetalleEntrada> actualizar(@PathVariable Long detalleEntradaId,
                                                      @RequestBody DetalleEntrada detalleEntrada) {
        return ResponseEntity.ok(detalleEntradaService.actualizarDetalleEntrada(detalleEntradaId, detalleEntrada));
    }
}
