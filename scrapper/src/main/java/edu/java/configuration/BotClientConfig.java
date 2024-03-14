package edu.java.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BotClientConfig {
    private final ApplicationConfig applicationConfig;

    public BotClientConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean("botWebClient")
    public WebClient botWebClient() {
        return WebClient
            .builder()
            .baseUrl(applicationConfig.botApiUrl())
            .build();
    }
}
