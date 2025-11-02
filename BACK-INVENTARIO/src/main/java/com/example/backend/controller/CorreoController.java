package com.example.backend.controller;

import com.example.backend.service.ReclamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CorreoController {

    private final ReclamoService reclamoService;

    @PostMapping("/reclamo/{id}/enviar-disculpas")
    public ResponseEntity<String> enviarDisculpas(@PathVariable Long id, @RequestBody String mensaje) {
        reclamoService.enviarDisculpasReclamo(id, mensaje);
        return ResponseEntity.ok("Correo de disculpas enviado correctamente");

    }

}
