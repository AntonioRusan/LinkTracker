package edu.scrapper.kafka;

import api.bot.models.LinkUpdate;
import edu.java.ScrapperApplication;
import edu.java.configuration.ApplicationConfig;
import edu.java.services.updates_notification.UpdatesNotificationService;
import edu.scrapper.database.IntegrationTest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = {ScrapperApplication.class}, properties = {"app.use-queue=true"})
public class KafkaUpdateNotificationTest extends IntegrationTest {

    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private UpdatesNotificationService updatesNotificationService;
    private static final String testUrl = "https://stackoverflow.com/questions/1/test-question";

    @Test
    void sendUpdateNotificationTest() throws URISyntaxException {
        LinkUpdate updateRequest = new LinkUpdate(
            1L,
            new URI(testUrl),
            "test question",
            List.of(1L)
        );

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bot-test");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        KafkaConsumer<String, LinkUpdate> consumer = new KafkaConsumer<>(
            props,
            new StringDeserializer(),
            new JsonDeserializer<>(LinkUpdate.class)
        );
        consumer.subscribe(List.of(applicationConfig.linkUpdatesTopicName()));

        updatesNotificationService.sendUpdateNotification(updateRequest);

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
