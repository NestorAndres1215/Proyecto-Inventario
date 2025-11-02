package com.example.backend.controller;

import com.example.backend.dto.LoginRequestDTO;

import com.example.backend.security.UserDetailsServiceImpl;
import com.example.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@Controller
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthService authService;



    @PostMapping("/generate-token")
    public ResponseEntity<?> generarToken(@RequestBody LoginRequestDTO jwtRequest) throws Exception {
        try {
            return ResponseEntity.ok(authService.login(jwtRequest));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERRRO SOLICITUD");
        }
    }


    @GetMapping("/actual-usuario")
    public ResponseEntity<?> obtenerUsuarioActual(Principal principal) {
        try {
            return ResponseEntity.ok(authService.actualUsuario(principal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR USUARIO ACTUAL");
        }
    }




}
