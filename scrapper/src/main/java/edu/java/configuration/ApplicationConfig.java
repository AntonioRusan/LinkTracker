package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import utils.retry.model.RetryConfig;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @Bean
    @NotNull
    Scheduler scheduler,
    AccessType databaseAccessType,
    String githubBaseUrl,
    String stackoverflowBaseUrl,
    String botApiUrl,
    RetryConfig retry
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public enum AccessType {
        JDBC,
        JPA,
        JOOQ
    }

}
