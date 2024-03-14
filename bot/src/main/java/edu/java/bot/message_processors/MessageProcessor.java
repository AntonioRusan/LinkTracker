package edu.java.bot.message_processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import java.util.List;

public interface MessageProcessor {
    List<Command> commandList();

    SendMessage processMessage(Update update);

    SetMyCommands createCommandsMenu();
}
