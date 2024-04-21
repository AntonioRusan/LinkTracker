package edu.java.services.updates_notification;

import api.bot.models.LinkUpdate;
import edu.java.clients.bot.BotClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpUpdatesNotificationServiceImpl implements UpdatesNotificationService {
    private final BotClient botClient;

    @Override
    public void sendUpdateNotification(LinkUpdate update) {
        botClient.sendUpdate(update);
    }
}
