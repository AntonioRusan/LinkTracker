package edu.java.bot.configuration.kafka;

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
public class KafkaDeadLetterQueueProducerConfig {
    @Bean
    public ProducerFactory<String, LinkUpdate> producerFactory(KafkaConsumerConfigProperties kafkaConsumerProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.bootstrapServers());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(
            props,
            new StringSerializer(),
            new JsonSerializer<LinkUpdate>()
        );
    }

    @Bean
    public KafkaTemplate<String, LinkUpdate> dlqKafkaTemplate(
        ProducerFactory<String, LinkUpdate> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
