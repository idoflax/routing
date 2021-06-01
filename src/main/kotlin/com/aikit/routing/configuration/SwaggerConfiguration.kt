package com.aikit.routing.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfiguration {

    @Bean
    fun instrumentServiceOpenAPI(): OpenAPI? {
        return OpenAPI()
            .components(Components())
            .info(
                Info()
                    .title("Routing Service")
                    .description("Aikit Coding Challenge")
                    .version("1.0")
            )
    }
}
