package com.kertas_kerja.data_kinerja.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        String serverUrl = "https://data-kinerja-api.zeabur.app" + contextPath;

        return new OpenAPI()
                .info(new Info()
                        .title("Data Kinerja API Docs")
                        .version("2.0.0")
                        .description("Pejabat Pengelola Informasi dan Dokumentasi"))
                .servers(List.of(
                        new Server()
                                .url(serverUrl)
                                .description("Development server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                                        .description("Enter username and password for Swagger UI access")
                        )
                );
    }
}
