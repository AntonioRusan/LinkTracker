package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.message_processors.MessageProcessor;
import java.util.List;

public class TelegramLinkTrackerUpdateListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final MessageProcessor messageProcessor;

    public TelegramLinkTrackerUpdateListener(TelegramBot telegramBot, MessageProcessor messageProcessor) {
        this.telegramBot = telegramBot;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public int process(List<Update> updateList) {
        updateList.forEach(System.out::println);
        updateList.forEach(update -> telegramBot.execute(messageProcessor.processMessage(update)));
        return CONFIRMED_UPDATES_ALL;
    }
}
