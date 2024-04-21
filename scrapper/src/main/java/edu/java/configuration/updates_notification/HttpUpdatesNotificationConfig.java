package edu.java.configuration.updates_notification;

import edu.java.clients.bot.BotClient;
import edu.java.services.updates_notification.HttpUpdatesNotificationServiceImpl;
import edu.java.services.updates_notification.UpdatesNotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("${app.useQueue:false}")
public class HttpUpdatesNotificationConfig {

    @Bean
    public UpdatesNotificationService updatesNotificationService(
        BotClient botClient
    ) {
        return new HttpUpdatesNotificationServiceImpl(botClient);
    }
}
