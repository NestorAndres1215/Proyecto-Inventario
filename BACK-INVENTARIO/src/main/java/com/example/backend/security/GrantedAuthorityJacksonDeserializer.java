package com.example.backend.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;


public class GrantedAuthorityJacksonDeserializer extends StdDeserializer<GrantedAuthority> {

    private static final long serialVersionUID = 1L;

    public GrantedAuthorityJacksonDeserializer() {
        super(GrantedAuthority.class);
    }

    @Override
    public GrantedAuthority deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode node = parser.getCodec().readTree(parser);

        // Validar estructura
        if (node == null || node.get("authority") == null) {
            throw new IOException("Formato JSON inválido para GrantedAuthority");
        }

        String authority = node.get("authority").asText();

        // Retorna la autoridad reconstruida
        return new SimpleGrantedAuthority(authority);
    }
}
