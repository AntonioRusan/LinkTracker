package edu.java.configuration.updates_notification;

import edu.java.services.kafka.ScrapperQueueProducer;
import edu.java.services.updates_notification.KafkaUpdatesNotificationServiceImpl;
import edu.java.services.updates_notification.UpdatesNotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("${app.useQueue:true}")
public class KafkaUpdatesNotificationConfig {

    @Bean
    public UpdatesNotificationService updatesNotificationService(
        ScrapperQueueProducer scrapperQueueProducer
    ) {
        return new KafkaUpdatesNotificationServiceImpl(scrapperQueueProducer);
    }
}
