package edu.java.configuration.client;

import edu.java.configuration.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import utils.retry.RetryUtils;

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
            .filter(RetryUtils.getRetryFilter("stackoverflow", applicationConfig.retry()))
            .baseUrl(applicationConfig.stackoverflowBaseUrl())
            .build();
    }
}
