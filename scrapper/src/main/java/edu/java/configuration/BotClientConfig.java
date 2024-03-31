package edu.java.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import static edu.java.retry.RetryUtils.getRetryFilter;

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
            .filter(getRetryFilter("bot", applicationConfig.retry()))
            .baseUrl(applicationConfig.botApiUrl())
            .build();
    }
}
