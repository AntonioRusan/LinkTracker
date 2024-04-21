package edu.java.configuration.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka")
public record KafkaProducerConfigProperties(
    String bootstrapServers,
    String clientId
) {
}
