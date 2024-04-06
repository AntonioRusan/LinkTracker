package edu.java.configuration.kafka;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka")
public record KafkaProducerConfigProperties(
    String bootstrapServers,
    String clientId,
    String acksMode,
    Duration deliveryTimeout,
    Integer lingerMs,
    Integer batchSize,
    Integer maxInFlightPerConnection,
    Boolean enableIdempotence,
    String topic,
    String securityProtocol,
    String saslMechanism,
    String saslJaasConfig
) {
}
