package com.example.backend.security;

import com.example.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(input)
                .orElseThrow(() ->
                        new UsernameNotFoundException("USUARIO NO ENCONTRADO"));
    }
}
