package com.ofss.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CustomerMS API")
                        .description("APIs for managing customer information, including registration, retrieval, updates, and deletion.")
                        .version("1.0.0"));
    }
}
