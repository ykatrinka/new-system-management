package ru.clevertec.newsservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.newsservice.util.DataSecurity;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Components securitySchemes = new Components()
                .addSecuritySchemes("JWT authorization",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name(DataSecurity.HEADER_AUTH));

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("JWT authorization", Arrays.asList("read", "write"));


        return new OpenAPI()
                .info(new Info()
                        .title("News API")
                        .description("Service for managing news")
                        .version("1.0.0"))
                .components(securitySchemes)
                .addSecurityItem(securityRequirement);
    }
}
