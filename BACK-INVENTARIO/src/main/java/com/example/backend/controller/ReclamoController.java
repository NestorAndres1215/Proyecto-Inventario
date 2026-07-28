package com.example.backend.controller;


import java.util.List;
import com.example.backend.dto.request.ReclamosRequest;
import com.example.backend.service.ReclamoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entity.Reclamos;


@RestController
@RequestMapping("/reclamo")
@Tag(name = "Reclamo")
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
    public ResponseEntity<Reclamos> agregar(@Valid @RequestBody ReclamosRequest reclamo) {
        return ResponseEntity.ok(reclamoService.agregarReclamo(reclamo));
    }

    @PostMapping("/activar/{id}")
    public ResponseEntity<Reclamos> activarReclamo(@PathVariable Long id) {
        return ResponseEntity.ok(reclamoService.activarReclamo(id));
    }

    @PostMapping("/desactivar/{id}")
    public ResponseEntity<Reclamos> desactivarReclamo(@PathVariable Long id) {
        return ResponseEntity.ok(reclamoService.desactivarReclamo(id));
    }
}