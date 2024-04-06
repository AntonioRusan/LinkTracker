package edu.java.services.kafka;

import api.bot.models.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    private final ApplicationConfig applicationConfig;
    private final static Logger LOGGER = LogManager.getLogger();

    public void send(LinkUpdate update) {
        CompletableFuture<SendResult<String, LinkUpdate>> future =
            kafkaTemplate.send(applicationConfig.linkUpdatesTopicName(), update);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                LOGGER.info("Sent message=[" + update + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                LOGGER.error("Unable to send message=[" + update + "] due to : " + ex.getMessage());
            }
        });
    }
}
