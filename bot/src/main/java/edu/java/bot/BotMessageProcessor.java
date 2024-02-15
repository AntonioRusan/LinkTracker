package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.StartCommand;
import java.util.List;

public class BotMessageProcessor implements MessageProcessor {

    @Override
    public List<Command> commandList() {
        return List.of(
            new StartCommand()
        );
    }

    @Override
    public SendMessage processMessage(Update update) {
        SendMessage response = null;
        for (Command command: commandList()) {
            if (update.message().text().equals(command.commandName())) {
                response = command.handleCommand(update);
            }
        }
        return response;
    }
}
