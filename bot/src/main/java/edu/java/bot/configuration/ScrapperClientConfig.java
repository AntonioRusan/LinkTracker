package edu.java.bot.configuration;

import edu.java.bot.WebClientErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ScrapperClientConfig {
    private final ApplicationConfig applicationConfig;

    public ScrapperClientConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean("scrapperWebClient")
    public WebClient gitHubWebClient() {
        return WebClient
            .builder()
            .filter(WebClientErrorHandler.errorHandler())
            .baseUrl(applicationConfig.scrapperApiUrl())
            .build();
    }
}
