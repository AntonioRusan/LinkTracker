package edu.java.clients.bot;

import edu.java.clients.bot.models.LinkUpdate;

public interface BotClient {

    void sendUpdate(LinkUpdate request);
}
