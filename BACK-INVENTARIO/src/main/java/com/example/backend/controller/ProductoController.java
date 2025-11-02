package com.example.backend.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.backend.service.ProductoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.backend.entity.Producto;

@RestController
@RequestMapping("/producto")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE}, allowedHeaders = "*")
@RequiredArgsConstructor
public class ProductoController {


    private final ProductoService productoService;

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodosLosProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activadas")
    public List<Producto> obtenerActivadas() {
        return productoService.obtenerProductosActivados();
    }

    @GetMapping("/desactivadas")
    public List<Producto> obtenerDesactivadas() {
        return productoService.obtenerProductosDesactivados();
    }


    @PostMapping("/")
    public ResponseEntity<Producto> agregarProducto(
            @RequestParam String nombre,
            @RequestParam String precio,
            @RequestParam String descripcion,
            @RequestParam int stock,
            @RequestParam String ubicacion,
            @RequestParam(value = "estado", required = false) Boolean estado,
            @RequestParam Long proveedorId) {

        Producto productoGuardado = productoService.agregarProducto(nombre, precio, descripcion, stock, ubicacion, estado, proveedorId);

        if (productoGuardado == null) {
            return ResponseEntity.notFound().build(); // proveedor no encontrado
        }

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(productoGuardado.getProductoId())
                        .toUri()
        ).body(productoGuardado);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String precio,
            @RequestParam String descripcion,
            @RequestParam int stock,
            @RequestParam String ubicacion,
            @RequestParam Long proveedorId) {

        Producto actualizado = productoService.actualizarProducto(id, nombre, precio, descripcion, stock, ubicacion, proveedorId);

        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/activar/{id}")
    public ResponseEntity<Map<String, String>> activarProducto(@PathVariable Long id) {
        boolean activado = productoService.activarProducto(id);
        Map<String, String> response = new HashMap<>();

        response.put("mensaje", "Producto activado con éxito");
        return ResponseEntity.ok(response);

    }

    @PostMapping("/desactivar/{id}")
    public ResponseEntity<Map<String, String>> desactivarProducto(@PathVariable Long id) {
        boolean desactivado = productoService.desactivarProducto(id);
        Map<String, String> response = new HashMap<>();

        response.put("mensaje", "Producto desactivado con éxito");
        return ResponseEntity.ok(response);

    }

}
