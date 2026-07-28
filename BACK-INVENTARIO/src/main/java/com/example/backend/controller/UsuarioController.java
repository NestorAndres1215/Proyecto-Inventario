package com.example.backend.controller;


import com.example.backend.dto.request.UsuarioRequest;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping("/guardar-normal")
    public ResponseEntity<Usuario> guardarNormal(@Valid @RequestBody UsuarioRequest admin) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(admin));
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Usuario> buscarPorTelefono(@PathVariable String telefono) {
        return ResponseEntity.ok(usuarioService.buscarPorTelefono(telefono));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        return  ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @PostMapping("/guardar-admin")
    public ResponseEntity<?> guardarAdmin(@Valid @RequestBody UsuarioRequest admin) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(admin));
    }

    @GetMapping("/admin/activadas")
    public ResponseEntity<List<Usuario>> obtenerAdminActivadas() {
        return ResponseEntity.ok(usuarioService.listarUsuarioAdminActivado());
    }

    @GetMapping("/admin/desactivadas")
    public ResponseEntity<List<Usuario>> obtenerAdminDesactivadas() {
        return ResponseEntity.ok(usuarioService.listarUsuarioAdminDesactivado());
    }

    @GetMapping("/normal/activadas")
    public ResponseEntity<List<Usuario>> obtenerNormalActivadas() {
        return ResponseEntity.ok(usuarioService.listarUsuarioNormalActivado());
    }

    @GetMapping("/normal/desactivadas")
    public ResponseEntity<List<Usuario>> obtenerNormalDesactivadas() {
        return ResponseEntity.ok(usuarioService.listarUsuarioNormalDesactivado());
    }

    @GetMapping("/listarId/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.listarPorId(id));
    }

    @PutMapping("/desactivar/{id}")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(usuarioService.eliminarUsuario(id));
    }

    @PutMapping("/activar/{id}")
    public ResponseEntity<Usuario> activarUsuario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(usuarioService.activarUsuario(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.listarPorId(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> buscarPorUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.buscarPorUsername(username));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<Usuario> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(usuarioService.buscarPorDni(dni));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Usuario>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> usuariosActivos() {
        return ResponseEntity.ok(usuarioService.usuariosActivos());
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Usuario>> usuariosInactivos() {
        return ResponseEntity.ok(usuarioService.usuariosInactivos());
    }
    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Usuario>> buscarPorApellido(@PathVariable String apellido) {
        return ResponseEntity.ok(usuarioService.buscarPorApellido(apellido));
    }
    @GetMapping("/rol/{rolNombre}/estado/{estado}")
    public ResponseEntity<List<Usuario>> buscarPorRolYEstado(
            @PathVariable String rolNombre,
            @PathVariable boolean estado) {

        Rol rol = usuarioService.getRolByNombre(rolNombre);
        return ResponseEntity.ok(usuarioService.buscarPorRolYEstado(rol, estado));
    }
}
