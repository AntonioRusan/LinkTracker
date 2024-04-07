package edu.java.bot.services.kafka;

import api.bot.models.LinkUpdate;
import edu.java.bot.handlers.LinkUpdatesHandler;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotQueueConsumer {

    private final LinkUpdatesHandler linkUpdatesHandler;
    private final static Logger LOGGER = LogManager.getLogger();

    @KafkaListener(topics = "${app.scrapper-topic-name}")
    public void listen(LinkUpdate update, Acknowledgment acknowledgment) {
        LOGGER.info("Получено из очереди: {}", update);
        linkUpdatesHandler.handleLinkUpdate(update);
        acknowledgment.acknowledge();
    }
}
