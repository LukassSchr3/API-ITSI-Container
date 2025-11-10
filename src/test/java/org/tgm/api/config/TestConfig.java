package org.tgm.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

/**
 * Test-Konfiguration für Mock-Beans in Tests
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public WebClient webClient() {
        return mock(WebClient.class);
    }
}

