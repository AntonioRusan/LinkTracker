package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramLinkTrackerBot {

    private final TelegramBot telegramBot;
    private final MessageProcessor messageProcessor = new BotMessageProcessor();

    @Autowired
    public TelegramLinkTrackerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    private void initializeTelegramBot() {
        telegramBot.execute(messageProcessor.createCommandsMenu());
        telegramBot.setUpdatesListener(new TelegramLinkTrackerUpdateListener(telegramBot, messageProcessor));
    }

}
