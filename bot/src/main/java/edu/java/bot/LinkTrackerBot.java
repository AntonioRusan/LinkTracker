package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import java.util.List;

public class LinkTrackerBot implements Bot {
    private final MessageProcessor messageProcessor = new BotMessageProcessor();

    @Override
    public int process(List<Update> updateList) {
        updateList.forEach(System.out::println);
        //updateList.forEach(update -> telegramBot.execute(messageProcessor.processMessage(update)));
        return CONFIRMED_UPDATES_ALL;
    }
}
