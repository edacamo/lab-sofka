package com.edacamo.msaccounts.infrastructure.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod") // Solo habilitado si el perfil activo NO es 'prod'
public class SwaggerConfig {

    // Define el grupo principal de endpoints públicos
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**") // Documenta todos los endpoints
                .build();
    }

    // Información general del API
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API MICROSERVICE-ACCOUNTS")
                        .description("Documentación interactiva para los servicios REST de microservice-accounts")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .url("https://maveluzsoft.com")
                                .email("soporte@maveluzsoft.com"))
                        .license(new License()
                                .name("Licencia MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
