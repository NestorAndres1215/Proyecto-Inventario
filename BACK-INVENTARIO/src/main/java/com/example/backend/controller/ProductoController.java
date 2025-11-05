package com.example.backend.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.backend.dto.ProductoDTO;
import com.example.backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Producto> agregarProducto(@RequestBody ProductoDTO productoDTO) {
        Producto productoGuardado = productoService.agregarProducto(productoDTO);
        return ResponseEntity.ok(productoGuardado);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @RequestBody ProductoDTO productoDTO) {

        Producto actualizado = productoService.actualizarProducto(productoDTO);

            return ResponseEntity.ok(actualizado);

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

    // ------------------ STOCK ------------------
    @GetMapping("/mayor-stock")
    public ResponseEntity<Producto> productoConMayorStock() {
        Producto producto = productoService.productoConMayorStock();
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.noContent().build();
    }

    @GetMapping("/menor-stock")
    public ResponseEntity<Producto> productoConMenorStock() {
        Producto producto = productoService.productoConMenorStock();
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.noContent().build();
    }

    // ------------------ POR PROVEEDOR ------------------
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Producto>> listarPorProveedor(@PathVariable Long proveedorId) {
        List<Producto> productos = productoService.listarProductosPorProveedor(proveedorId);
        return ResponseEntity.ok(productos);
    }

    // ------------------ TOP 10 MÁS BARATOS ------------------
    @GetMapping("/top10-mas-baratos")
    public ResponseEntity<List<Producto>> top10ProductosMasBaratos() {
        List<Producto> productos = productoService.top10ProductosMasBaratos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/top10-mas-baratos-activos")
    public ResponseEntity<List<Producto>> top10ProductosMasBaratosActivos() {
        List<Producto> productos = productoService.top10ProductosMasBaratosActivos();
        return ResponseEntity.ok(productos);
    }
}
