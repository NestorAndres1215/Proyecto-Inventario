package com.example.backend.controller;


import com.example.backend.dto.UsuarioRequestDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE}, allowedHeaders = "*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;



    // registrar usuarios normales
    @PostMapping("/guardar-normal")
    public ResponseEntity<?> guardarNormal(@RequestBody UsuarioRequestDTO admin) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(admin));
    }

    // registrar usuarios admin
    @PostMapping("/guardar-admin")
    public ResponseEntity<?> guardarAdmin(@RequestBody UsuarioRequestDTO admin) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(admin));
    }

    // 🔹 Administradores ACTIVADOS
    @GetMapping("/admin/activadas")
    public ResponseEntity<List<Usuario>> obtenerAdminActivadas() {
        List<Usuario> usuariosAdminActivados = usuarioService.listarUsuarioAdminActivado();
        return ResponseEntity.ok(usuariosAdminActivados);
    }

    // 🔹 Administradores DESACTIVADOS
    @GetMapping("/admin/desactivadas")
    public ResponseEntity<List<Usuario>> obtenerAdminDesactivadas() {
        List<Usuario> usuariosAdminDesactivados = usuarioService.listarUsuarioAdminDesactivado();
        return ResponseEntity.ok(usuariosAdminDesactivados);
    }

    // 🔹 Usuarios normales ACTIVADOS
    @GetMapping("/normal/activadas")
    public ResponseEntity<List<Usuario>> obtenerNormalActivadas() {
        List<Usuario> usuariosNormalesActivados = usuarioService.listarUsuarioNormalActivado();
        return ResponseEntity.ok(usuariosNormalesActivados);
    }

    // 🔹 Usuarios normales DESACTIVADOS
    @GetMapping("/normal/desactivadas")
    public ResponseEntity<List<Usuario>> obtenerNormalDesactivadas() {
        List<Usuario> usuariosNormalesDesactivados = usuarioService.listarUsuarioNormalDesactivado();
        return ResponseEntity.ok(usuariosNormalesDesactivados);
    }

    //LISTAR POR ID
    @GetMapping("/listarId/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.listarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // 🔹 Desactivar usuario (estado = false)
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<String> desactivarUsuario(@PathVariable("id") Long id) {
        Usuario usuarioActualizado = usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("SE DESACTIVO CORRECTAMENTE");
    }

    // 🔹 Activar usuario (estado = true)
    @PutMapping("/activar/{id}")
    public ResponseEntity<Usuario> activarUsuario(@PathVariable("id") Long id) {
        Usuario usuarioActualizado = usuarioService.activarUsuario(id);
        return ResponseEntity.ok(usuarioActualizado);
    }
}
