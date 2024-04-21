package edu.java.configuration.client;

import edu.java.configuration.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import utils.retry.RetryUtils;

@Configuration
@SuppressWarnings("MagicNumber")
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
            .filter(RetryUtils.getRetryFilter("github", applicationConfig.retry()))
            .baseUrl(applicationConfig.githubBaseUrl())
            .build();
    }
}
