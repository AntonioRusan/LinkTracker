package edu.java.services.updates_notification;

import api.bot.models.LinkUpdate;

public interface UpdatesNotificationService {
    void sendUpdateNotification(LinkUpdate update);
}
