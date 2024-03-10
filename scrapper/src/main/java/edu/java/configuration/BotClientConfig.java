package edu.java.configuration;

import edu.java.handlers.WebClientErrorHandler;
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
    public WebClient gitHubWebClient() {
        return WebClient
            .builder()
            .filter(WebClientErrorHandler.errorHandler())
            .baseUrl(applicationConfig.botApiUrl())
            .build();
    }
}
