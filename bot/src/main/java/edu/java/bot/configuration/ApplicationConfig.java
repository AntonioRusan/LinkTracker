package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import utils.retry.model.RetryConfig;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotEmpty
    String scrapperApiUrl,
    @NotEmpty
    String scrapperTopicName,
    RetryConfig retry
) {

}
