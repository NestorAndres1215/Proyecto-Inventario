package com.example.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.entity.Proveedor;

import com.example.backend.repository.ProveedorRepository;

@RestController
@RequestMapping("/proveedor")

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE}, allowedHeaders = "*")
@RequiredArgsConstructor
public class ProveedorController {


    private final ProveedorRepository proveedorRepository;
    private final ProveedorService proveedorService;

    // Metodo Listar
    @GetMapping
    public List<Proveedor> obtenerProveedor() {
        return proveedorService.listarTodos();
    }

    // Metodo Listar por Id
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerProveedorPorId(@PathVariable Long id) {
        return proveedorService.obtenerPorId(id)
                .map(proveedor -> ResponseEntity.ok(proveedor))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Metodo Listar desactivadas
    @GetMapping("/desactivadas")
    public List<Proveedor> obtenerProveedorDesactivadas() {
        return proveedorService.findByEstadoIsFalse();
    }

    // Metodo Listar activadas
    @GetMapping("/activadas")
    public List<Proveedor> obtenerProveedorActivadas() {
        return proveedorService.findByEstadoIsTrue();
    }

    // crear proveedor
    @PostMapping("/")
    public ResponseEntity<?> agregarProveedor(@RequestBody ProveedorDTO proveedorDTO) {
        Proveedor creado = proveedorService.crearProveedor(proveedorDTO);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@RequestBody ProveedorDTO proveedorDTO) {
        Proveedor actualizado = proveedorService.actualizarProveedor(proveedorDTO);
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/activar/{id}")
    public ResponseEntity<Map<String, String>> activarProveedor(@PathVariable Long id) {
        boolean activado = proveedorService.activarProveedor(id);
        Map<String, String> response = new HashMap<>();
        if (activado) {
            response.put("mensaje", "Proveedor activado con éxito");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/desactivar/{id}")
    public ResponseEntity<Map<String, String>> desactivarProveedor(@PathVariable Long id) {
        boolean desactivado = proveedorService.desactivarProveedor(id);
        Map<String, String> response = new HashMap<>();
        if (desactivado) {
            response.put("mensaje", "Proveedor desactivado con éxito");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
