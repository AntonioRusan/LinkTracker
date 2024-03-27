package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.services.bot_command.BotCommandService;

public class StartCommand implements Command {
    private BotCommandService botCommandService;

    public StartCommand() {
    }

    public StartCommand(BotCommandService botCommandService) {
        this.botCommandService = botCommandService;
    }

    @Override
    public String commandName() {
        return "/start";
    }

    @Override
    public String description() {
        return "Регистрация пользователя";
    }

    @Override
    public SendMessage handleCommand(Update update) {
        Long chatId = update.message().chat().id();
        String firstName = update.message().chat().firstName();
        String lastName = update.message().chat().lastName();
        String response = "Привет, " + firstName
            + " " + lastName + "!\n" + botCommandService.registerChat(chatId);
        return new SendMessage(chatId, response);
    }
}
