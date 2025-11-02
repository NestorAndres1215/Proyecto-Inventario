package com.example.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.service.ReclamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.backend.entity.Reclamos;

import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/reclamo")

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE}, allowedHeaders = "*")
@RequiredArgsConstructor
public class ReclamoController {


    private final ReclamoService reclamoService;



    @GetMapping
    public List<Reclamos> obtenerProveedor() {
        return reclamoService.obtenerTodosLosReclamos();
    }

    @GetMapping("/{id}")
    public Reclamos obtenerReclamoPorId(@PathVariable Long id) {
        return reclamoService.obtenerReclamoPorId(id);
    }

    @GetMapping("/desactivadas")
    public List<Reclamos> obtenerDesactivadas() {
        return reclamoService.obtenerReclamosDesactivados();
    }

    @GetMapping("/activadas")
    public List<Reclamos> obtenerActivadas() {
        return reclamoService.obtenerReclamosActivados();
    }

    @PostMapping("/")
    public ResponseEntity<Reclamos> agregar(@RequestBody Reclamos reclamo) {
        Reclamos reclamoGuardado = reclamoService.agregarReclamo(reclamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(reclamoGuardado);
    }

    @PostMapping("/activar/{id}")
    public ResponseEntity<Map<String, String>> activarReclamo(@PathVariable Long id) {
        boolean activado = reclamoService.activarReclamo(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Reclamo activado con éxito");
        return ResponseEntity.ok(response);

    }

    @PostMapping("/desactivar/{id}")
    public ResponseEntity<Map<String, String>> desactivarReclamo(@PathVariable Long id) {
        boolean desactivado = reclamoService.desactivarReclamo(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Reclamo desactivado con éxito");
        return ResponseEntity.ok(response);
    }
}