package com.example.backend.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Security") // Título del API
                        .version("1.0")    // Versión
                        .description("""
                                Documentación de la API REST del módulo **AUTH SERVICE**.
                                Este servicio gestiona la autenticación, autorización y
                                administración de roles y usuarios dentro del ecosistema **Red Social**.
                                """) // Descripción larga
                        .contact(new Contact()
                                .name("Equipo de Desarrollo - Red Social")
                                .email("soporte@redsocial.com")
                                .url("https://redsocial.com"))
                )
                // Configuración de seguridad JWT
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                // Seguridad global: aplica a todos los endpoints
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
