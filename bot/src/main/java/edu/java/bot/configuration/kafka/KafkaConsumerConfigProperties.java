package edu.java.bot.configuration.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka")
public record KafkaConsumerConfigProperties(
    String bootstrapServers,
    String groupId,
    String autoOffsetReset,
    Boolean enableAutoCommit,
    Integer concurrency
) {

}
