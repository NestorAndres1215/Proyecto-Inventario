package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.response.TokenResponse;
import com.example.backend.entity.Usuario;
import com.example.backend.security.UserDetailsServiceImpl;
import com.example.backend.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthService authService;

    @PostMapping("/generate-token")
    public ResponseEntity<TokenResponse> generarToken(@Valid @RequestBody LoginRequest request)  {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/actual-usuario")
    public ResponseEntity<Usuario> obtenerUsuarioActual(Principal principal) {
        return ResponseEntity.ok(authService.actualUsuario(principal));
    }

}
