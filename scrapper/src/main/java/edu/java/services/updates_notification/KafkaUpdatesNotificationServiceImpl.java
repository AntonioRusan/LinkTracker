package edu.java.services.updates_notification;

import api.bot.models.LinkUpdate;
import edu.java.services.kafka.ScrapperQueueProducer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KafkaUpdatesNotificationServiceImpl implements UpdatesNotificationService {
    private final ScrapperQueueProducer scrapperQueueProducer;

    @Override
    public void sendUpdateNotification(LinkUpdate update) {
        scrapperQueueProducer.send(update);
    }
}
