package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;

@Service
public class TelegramLinkTrackerBot {

    private final TelegramBot telegramBot;
    private final ApplicationConfig applicationConfig;
    private final MessageProcessor messageProcessor = new BotMessageProcessor();

    @Autowired
    public TelegramLinkTrackerBot(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        this.telegramBot = new TelegramBot(applicationConfig.telegramToken());
        telegramBot.setUpdatesListener(updateList -> {
            updateList.forEach(System.out::println);
            updateList.forEach(update -> telegramBot.execute(messageProcessor.processMessage(update)));
            return CONFIRMED_UPDATES_ALL;
        });
    }

}
