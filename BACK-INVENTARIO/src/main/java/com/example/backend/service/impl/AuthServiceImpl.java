package com.example.backend.service.impl;

import com.example.backend.constants.GlobalErrorMessages;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.response.TokenResponse;
import com.example.backend.entity.Usuario;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.security.JwtUtils;
import com.example.backend.security.UserDetailsServiceImpl;
import com.example.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    public TokenResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());

        return TokenResponse.builder()
                .token(jwtUtils.generateToken(userDetails))
                .build();
    }

    @Override
    public Usuario actualUsuario(Principal principal) {

        if (principal == null || principal.getName() == null) {
            throw new ResourceNotFoundException(GlobalErrorMessages.NO_AUTORIZADO);
        }

        return (Usuario) userDetailsService.loadUserByUsername(principal.getName());
    }
}