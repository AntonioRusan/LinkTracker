package edu.java.bot.message_processors;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.repositories.UserRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BotMessageProcessor implements MessageProcessor {
    private final UserRepository userRepository;
    public final List<String> commandDescriptionList = List.of(
        new StartCommand().commandNameAndDescription(),
        new ListCommand().commandNameAndDescription(),
        new HelpCommand().commandNameAndDescription(),
        new TrackCommand().commandNameAndDescription(),
        new UntrackCommand().commandNameAndDescription()
    );
    private final List<Command> commandList;

    public BotMessageProcessor(UserRepository userRepository) {
        this.userRepository = userRepository;
        commandList = List.of(
            new StartCommand(userRepository),
            new ListCommand(userRepository),
            new HelpCommand(commandDescriptionList),
            new TrackCommand(userRepository),
            new UntrackCommand(userRepository)
        );
    }

    @Override
    public List<Command> commandList() {
        return commandList;
    }

    @Override
    public SendMessage processMessage(Update update) {
        SendMessage response = null;
        Boolean foundCommandFlag = false;
        for (Command command : commandList()) {
            if (update.message().text().startsWith(command.commandName())) {
                response = command.handleCommand(update);
                foundCommandFlag = true;
            }
        }
        if (!foundCommandFlag) {
            response = createErrorMessage(update, "Недопустимая команда!");
        }
        return response;
    }

    public SetMyCommands createCommandsMenu() {
        return new SetMyCommands(commandList().stream().map(Command::toBotCommand).toArray(BotCommand[]::new));
    }

    private SendMessage createErrorMessage(Update update, String message) {
        Long chatId = update.message().chat().id();
        return new SendMessage(chatId, message);
    }
}
