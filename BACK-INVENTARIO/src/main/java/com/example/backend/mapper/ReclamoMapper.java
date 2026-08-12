package com.example.backend.mapper;

import com.example.backend.dto.request.ReclamosRequest;
import com.example.backend.entity.Reclamos;
import com.example.backend.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ReclamoMapper {

    public Reclamos toEntity(
            ReclamosRequest dto,
            Usuario usuario
    ) {

        return Reclamos.builder()
                .asunto(dto.getAsunto())
                .usuario(usuario)
                .estado(true)
                .build();
    }
}