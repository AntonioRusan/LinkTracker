package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class StartCommand implements Command {
    @Override
    public String commandName() {
        return "/start";
    }

    @Override
    public String description() {
        return "Start command";
    }

    @Override
    public SendMessage handleCommand(Update update) {
        Long chatId = update.message().chat().id();
        return new SendMessage(chatId, "Привет, " + update.message().chat().firstName() + " " + update.message().chat().lastName() + "!");
    }
}
