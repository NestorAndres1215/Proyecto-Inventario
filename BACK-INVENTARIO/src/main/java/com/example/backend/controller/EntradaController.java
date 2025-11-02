package com.example.backend.controller;

import com.example.backend.entity.DetalleEntrada;
import com.example.backend.service.DetalleEntradaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;



@RestController
@RequestMapping("/entradas")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE}, allowedHeaders = "*")
@RequiredArgsConstructor
public class EntradaController {

    private final DetalleEntradaService detalleEntradaService;



    @PostMapping("/")
    public ResponseEntity<List<DetalleEntrada>> crearEntrada(@RequestBody List<DetalleEntrada> listaDetalleEntrada) {
        List<DetalleEntrada> guardados = detalleEntradaService.crearDetalleEntrada(listaDetalleEntrada);
        return ResponseEntity.ok(guardados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleEntrada> obtenerPorId(@PathVariable Long id) {
        DetalleEntrada detalle = detalleEntradaService.obtenerPorId(id);
        return ResponseEntity.ok(detalle);
    }

    @GetMapping
    public ResponseEntity<List<DetalleEntrada>> obtenerTodos() {
        return ResponseEntity.ok(detalleEntradaService.obtenerTodos());
    }

    @PutMapping("/{detalleEntradaId}")
    public ResponseEntity<DetalleEntrada> actualizar(@PathVariable Long detalleEntradaId,
                                                      @RequestBody DetalleEntrada detalleEntrada) {
        DetalleEntrada actualizado = detalleEntradaService.actualizarDetalleEntrada(detalleEntradaId, detalleEntrada);
        return ResponseEntity.ok(actualizado);
    }
}
