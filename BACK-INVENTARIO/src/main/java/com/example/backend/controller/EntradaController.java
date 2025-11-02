package com.example.backend.controller;

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

import com.example.backend.entity.Detalle_Entrada;

@RestController
@RequestMapping("/entradas")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE}, allowedHeaders = "*")
@RequiredArgsConstructor
public class EntradaController {

    private final DetalleEntradaService detalleEntradaService;



    @PostMapping("/")
    public ResponseEntity<List<Detalle_Entrada>> crearEntrada(@RequestBody List<Detalle_Entrada> listaDetalleEntrada) {
        List<Detalle_Entrada> guardados = detalleEntradaService.crearDetalleEntrada(listaDetalleEntrada);
        return ResponseEntity.ok(guardados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Detalle_Entrada> obtenerPorId(@PathVariable Long id) {
        Detalle_Entrada detalle = detalleEntradaService.obtenerPorId(id);
        return ResponseEntity.ok(detalle);
    }

    @GetMapping
    public ResponseEntity<List<Detalle_Entrada>> obtenerTodos() {
        return ResponseEntity.ok(detalleEntradaService.obtenerTodos());
    }

    @PutMapping("/{detalleEntradaId}")
    public ResponseEntity<Detalle_Entrada> actualizar(@PathVariable Long detalleEntradaId,
                                                      @RequestBody Detalle_Entrada detalleEntrada) {
        Detalle_Entrada actualizado = detalleEntradaService.actualizarDetalleEntrada(detalleEntradaId, detalleEntrada);
        return ResponseEntity.ok(actualizado);
    }
}
