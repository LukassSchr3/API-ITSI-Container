package org.tgm.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BackendClientConfig {

    @Value("${backend.url:http://localhost:8081}")
    private String backendUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(backendUrl)
                .build();
    }
}

