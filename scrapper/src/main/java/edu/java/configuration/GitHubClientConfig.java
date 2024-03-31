package edu.java.configuration;

import edu.java.handlers.WebClientErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubClientConfig {
    private final ApplicationConfig applicationConfig;

    public GitHubClientConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean("gitHubWebClient")
    public WebClient gitHubWebClient() {
        return WebClient
            .builder()
            .exchangeStrategies(
                ExchangeStrategies
                    .builder()
                    .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)
                    )
                    .build()
            )
            .filter(WebClientErrorHandler.errorHandler())
            .baseUrl(applicationConfig.githubBaseUrl())
            .build();
    }
}
