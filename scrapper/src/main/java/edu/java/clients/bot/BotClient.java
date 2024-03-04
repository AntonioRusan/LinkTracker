package edu.java.clients.bot;

import edu.java.clients.bot.model.LinkUpdate;

public interface BotClient {

    void sendUpdate(LinkUpdate request);
}
