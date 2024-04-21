package edu.java.bot.configuration.kafka;

import api.bot.models.LinkUpdate;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, LinkUpdate> consumerFactory(KafkaConsumerConfigProperties kafkaConsumerProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.bootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.groupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProperties.autoOffsetReset());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerProperties.enableAutoCommit());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdate.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            new JsonDeserializer<>(LinkUpdate.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdate> kafkaListenerContainerFactory(
        KafkaConsumerConfigProperties kafkaConsumerProperties
    ) {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdate> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(kafkaConsumerProperties));
        factory.setConcurrency(kafkaConsumerProperties.concurrency());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
