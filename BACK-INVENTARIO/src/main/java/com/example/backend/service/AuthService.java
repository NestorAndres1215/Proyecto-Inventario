package com.example.backend.service;


import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.TokenResponseDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.security.JwtUtils;

import com.example.backend.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuthService   {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    // Constantes para mensajes
    private static final String MSG_USUARIO_NO_ENCONTRADO = "Usuario no encontrado";
    private static final String MSG_IDENTIFICADOR_VACIO = "El identificador no puede estar vacío";
    private static final String MSG_CREDENCIALES_INVALIDAS = "Usuario o contraseña incorrectos";
    private static final String MSG_ERROR_AUTENTICACION = "Error al iniciar sesión";
    private static final String MSG_USUARIO_NO_AUTORIZADO = "Usuario no autorizado";


    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO) {
        validarIdentificador(loginRequestDTO.getLogin());

        try {
            // Autenticación con Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getLogin(), loginRequestDTO.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getLogin());
            String token = jwtUtils.generateToken(userDetails);

            return new TokenResponseDTO(token);

        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, MSG_CREDENCIALES_INVALIDAS, ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_ERROR_AUTENTICACION, ex);
        }
    }


    public Usuario actualUsuario(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, MSG_USUARIO_NO_AUTORIZADO);
        }

        return (Usuario) userDetailsService.loadUserByUsername(principal.getName());
    }

    private void validarIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_IDENTIFICADOR_VACIO);
        }

        if (!usuarioService.usuarioExistePorUsername(identificador)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_USUARIO_NO_ENCONTRADO);
        }
    }
}