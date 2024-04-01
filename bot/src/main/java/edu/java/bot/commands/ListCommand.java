package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.services.bot_command.BotCommandService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListCommand implements Command {
    private final BotCommandService botCommandService;

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
        String trackedUrls = botCommandService.listLinks(chatId);
        String response = !trackedUrls.isEmpty() ? "Отслеживаемые ссылки:\n" + String.join("\n", trackedUrls)
            : "Нет отслеживаемых ссылок!";
        return new SendMessage(chatId, response);
    }
}
