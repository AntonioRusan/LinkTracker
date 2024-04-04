package edu.java.configuration.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "kafka")
public record KafkaProducerConfigProperties (
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
){
}
