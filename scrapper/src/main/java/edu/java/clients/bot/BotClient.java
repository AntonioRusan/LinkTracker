package edu.java.clients.bot;

import api.bot.models.LinkUpdate;

public interface BotClient {

    void sendUpdate(LinkUpdate request);
}
