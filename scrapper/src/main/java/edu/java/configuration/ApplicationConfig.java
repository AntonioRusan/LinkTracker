package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

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

    public record RetryConfig(
        List<RetryItem> retryItems
    ) {
        public record RetryItem(
            @NotNull String clientName,
            @NotNull RetryType type,
            int maxAttempts,
            Duration minBackoff,
            Duration maxBackoff,
            double jitter,
            Duration increment,
            List<Integer> errorCodes
        ) {
        }

        public enum RetryType {
            CONSTANT,
            LINEAR,
            EXPONENTIAL
        }
    }
}
