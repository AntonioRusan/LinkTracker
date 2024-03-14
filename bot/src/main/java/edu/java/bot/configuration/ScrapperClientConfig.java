package edu.java.bot.configuration;

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
    public WebClient scrapperWebClient() {
        return WebClient
            .builder()
            .baseUrl(applicationConfig.scrapperApiUrl())
            .build();
    }
}
