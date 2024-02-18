package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command {
    @Override
    public String commandName() {
        return "/list";
    }

    @Override
    public String description() {
        return "Вывести список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handleCommand(Update update) {
        Long chatId = update.message().chat().id();
        List<String> trackedUrls = new ArrayList<>();
        String response = !trackedUrls.isEmpty() ? "Отслеживаемые ссылки:\n" : "Нет отслеживаемых ссылок!";
        return new SendMessage(chatId, response);
    }
}
