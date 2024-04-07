package edu.java.configuration.kafka;

import api.bot.models.LinkUpdate;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfiguration {
    @Bean
    public ProducerFactory<String, LinkUpdate> producerFactory(
        KafkaProducerConfigProperties kafkaProducerConfigProperties
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfigProperties.bootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerConfigProperties.clientId());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigProperties.acksMode());
        props.put(
            ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
            (int) kafkaProducerConfigProperties.deliveryTimeout().toMillis()
        );
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigProperties.lingerMs());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigProperties.batchSize());
        props.put(
            ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
            kafkaProducerConfigProperties.maxInFlightPerConnection()
        );
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaProducerConfigProperties.enableIdempotence());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(
            props,
            new StringSerializer(),
            new JsonSerializer<LinkUpdate>()
        );
    }

    @Bean
    public KafkaTemplate<String, LinkUpdate> kafkaTemplate(ProducerFactory<String, LinkUpdate> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
