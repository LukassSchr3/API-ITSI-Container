package org.tgm.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:API Gateway}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " - REST API Dokumentation")
                        .version("1.0.0")
                        .description("REST API für die Kommunikation zwischen Frontend und Backend. " +
                                "Diese API verwaltet Container-Operationen basierend auf UserID und AufgabenID.")
                        .contact(new Contact()
                                .name("TGM Diplomprojekt")
                                .email("support@tgm.ac.at"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Lokale Entwicklungsumgebung"),
                        new Server()
                                .url("http://localhost:9090")
                                .description("Backend Server")
                ));
    }
}

