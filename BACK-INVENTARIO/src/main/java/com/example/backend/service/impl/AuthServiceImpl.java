package com.example.backend.service.impl;


import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.TokenResponseDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.service.AuthService;
import com.example.backend.service.UsuarioService;
import com.example.backend.security.JwtUtils;

import com.example.backend.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;



    @Override
    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO) {
        String identificador= loginRequestDTO.getLogin();
        validarIdentificador(identificador);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getLogin(), loginRequestDTO.getPassword()));
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(loginRequestDTO.getLogin());
            String token = this.jwtUtils.generateToken(userDetails);

            return new TokenResponseDTO(token);
        }catch (BadCredentialsException ex) {
            // Usuario o contraseña incorrectos
            throw new RuntimeException("Usuario o contraseña incorrectos", ex);
        } catch (Exception ex) {
            // Otros errores de autenticación
            throw new RuntimeException("Error al iniciar sesión", ex);
        }

    }
    @Override
    public Usuario actualUsuario(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new RuntimeException("Usuario no autorizado");
        }
        Usuario usuario = (Usuario) this.userDetailsService.loadUserByUsername(principal.getName());
        return usuario;
    }
    private void validarIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El identificador no puede estar vacío");
        }

        if (!usuarioService.usuarioExistePorUsername(identificador)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }


}
