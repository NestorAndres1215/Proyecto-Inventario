package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import com.example.backend.service.DetalleSalidaService;
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


import com.example.backend.entity.Detalle_Salida;

@RestController
@RequestMapping("/salidas")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE}, allowedHeaders = "*")
public class SalidaController {

    private final DetalleSalidaService detalleSalidaService;

    public SalidaController(DetalleSalidaService detalleSalidaService) {
        this.detalleSalidaService = detalleSalidaService;
    }

    @PostMapping("/")
    public ResponseEntity<List<Detalle_Salida>> crearDetalleSalida(@RequestBody List<Detalle_Salida> listaDetalleSalida) {
        try {
            List<Detalle_Salida> guardados = detalleSalidaService.crearDetalleSalida(listaDetalleSalida);
            return ResponseEntity.ok(guardados);
        } catch (Exception e) {

            return ResponseEntity.status(500).build();
        }
    }


    @PutMapping("/{detalleSalidaId}")
    public ResponseEntity<Map<String, Boolean>> actualizarDetalle(@PathVariable Long detalleSalidaId,
                                                                  @RequestBody Detalle_Salida detalleEntrada) {
        Map<String, Boolean> result = detalleSalidaService.actualizarDetalleSalida(detalleSalidaId, detalleEntrada);

        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{id}")
    public Detalle_Salida obtenerPorId(@PathVariable Long id) {
        return detalleSalidaService.obtenerPorId(id);
    }

    @GetMapping
    public List<Detalle_Salida> obtenerTodas() {
        return detalleSalidaService.obtenerTodas();
    }


}
