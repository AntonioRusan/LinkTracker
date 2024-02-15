package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String commandName();

    String description();

    SendMessage handleCommand(Update update);

    default BotCommand toApiCommand() {
        return new BotCommand(commandName(), description());
    }

}
