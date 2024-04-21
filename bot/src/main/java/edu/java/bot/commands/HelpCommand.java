package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements Command {
    private final List<String> commandList;

    public HelpCommand(List<String> commandList) {
        ArrayList<String> tmpCommands = new ArrayList<>(commandList);
        tmpCommands.add(this.commandNameAndDescription());
        this.commandList = tmpCommands;
    }

    @Override
    public String commandName() {
        return "/help";
    }

    @Override
    public String description() {
        return "Вывести список доступных команд";
    }

    @Override
    public SendMessage handleCommand(Update update) {
        Long chatId = update.message().chat().id();
        String response = "Список команд:\n" + String.join("\n", commandList);
        return new SendMessage(chatId, response);
    }
}
