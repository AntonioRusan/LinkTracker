package edu.java.bot.services.kafka;

import api.bot.models.LinkUpdate;
import edu.java.bot.handlers.LinkUpdatesHandler;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotQueueConsumer {

    private final LinkUpdatesHandler linkUpdatesHandler;
    private final static Logger LOGGER = LogManager.getLogger();

    @KafkaListener(topics = "${app.scrapper-topic-name}")
    @RetryableTopic(
        attempts = "1",
        dltStrategy = DltStrategy.FAIL_ON_ERROR,
        kafkaTemplate = "dlqKafkaTemplate"
    )
    public void listen(
        LinkUpdate update,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        Acknowledgment acknowledgment
    ) {
        LOGGER.info("Получено из очереди: {}. Сообщение: {}", topic, update);
        linkUpdatesHandler.handleLinkUpdate(update);
        acknowledgment.acknowledge();
    }

    @DltHandler
    public void handleDeadLetterQueue(
        LinkUpdate update,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info("Получено из очереди недоставленных сообщений: {}.", topic);
    }
}
