package edu.bot.kafka;

import api.bot.models.LinkUpdate;
import com.pengrad.telegrambot.TelegramBot;
import edu.bot.IntegrationTest;
import edu.java.bot.BotApplication;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.handlers.LinkUpdatesHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = {BotApplication.class})
@DirtiesContext
public class KafkaUpdatesConsumerTest extends IntegrationTest {
    @Autowired
    private ApplicationConfig applicationConfig;
    @MockBean
    private TelegramBot telegramBot;
    @MockBean
    private LinkUpdatesHandler linkUpdatesHandler;
    private static final String testUrl = "https://stackoverflow.com/questions/1/test-question";

    @Test
    void updatesConsumerTest() throws URISyntaxException {
        LinkUpdate updateRequest = new LinkUpdate(
            1L,
            new URI(testUrl),
            "test question",
            List.of(1L)
        );

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());

        KafkaProducer<String, LinkUpdate> producer = new KafkaProducer(
            props,
            new StringSerializer(),
            new JsonSerializer<LinkUpdate>()
        );
        producer.send(new ProducerRecord<>(applicationConfig.scrapperTopicName(), updateRequest));

        await()
            .pollInterval(Duration.ofMillis(60))
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> Mockito.verify(linkUpdatesHandler, Mockito.times(1))
                .handleLinkUpdate(updateRequest));
    }

    @Test
    void deadLetterQueueTest() throws URISyntaxException {
        LinkUpdate updateRequest = new LinkUpdate(
            1L,
            new URI(testUrl),
            "test question",
            List.of(1L)
        );

        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());

        KafkaProducer<String, LinkUpdate> producer = new KafkaProducer(
            producerProps,
            new StringSerializer(),
            new JsonSerializer<LinkUpdate>()
        );

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "bot-test");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdate.class);
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        KafkaConsumer<String, LinkUpdate> consumer = new KafkaConsumer(
            consumerProps,
            new StringDeserializer(),
            new JsonDeserializer<>(LinkUpdate.class)
        );
        consumer.subscribe(List.of(applicationConfig.scrapperTopicName() + "-dlt"));

        producer.send(new ProducerRecord<>(applicationConfig.scrapperTopicName(), updateRequest));
        Mockito.doThrow(RuntimeException.class).when(linkUpdatesHandler).handleLinkUpdate(updateRequest);

        await()
            .pollInterval(Duration.ofSeconds(5))
            .atMost(60, SECONDS)
            .untilAsserted(() -> {
                    ConsumerRecords<String, LinkUpdate> records = consumer.poll(Duration.ofMillis(60));
                    assertThat(records.count()).isEqualTo(1);
                    assertThat(records.iterator().next().value()).isEqualTo(updateRequest);
                }
            );
        consumer.unsubscribe();
    }
}
