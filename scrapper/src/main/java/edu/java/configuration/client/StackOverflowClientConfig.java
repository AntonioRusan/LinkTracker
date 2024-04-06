package edu.java.configuration.client;

import edu.java.configuration.ApplicationConfig;
import edu.java.handlers.WebClientErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class StackOverflowClientConfig {
    private final ApplicationConfig applicationConfig;

    public StackOverflowClientConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean("stackOverflowWebClient")
    public WebClient stackOverflowWebClient() {
        return WebClient
                .builder()
                .filter(WebClientErrorHandler.errorHandler())
                .baseUrl(applicationConfig.stackoverflowBaseUrl())
                .build();
    }
}
