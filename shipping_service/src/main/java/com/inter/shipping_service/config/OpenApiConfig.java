package com.inter.shipping_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Serviço de Remessa",
                version = "1.0",
                description = "API para realizar remessas com conversão de moeda"
        )
)

public class OpenApiConfig {
}
