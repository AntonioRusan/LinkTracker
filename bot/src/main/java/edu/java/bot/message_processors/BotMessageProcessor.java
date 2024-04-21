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
import edu.java.bot.services.bot_command.BotCommandService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BotMessageProcessor implements MessageProcessor {

    private List<Command> commandList;
    private final BotCommandService botCommandService;
    private final Counter handledUserMessagesCounter;

    public BotMessageProcessor(BotCommandService botCommandService, MeterRegistry meterRegistry) {
        this.botCommandService = botCommandService;
        this.commandList = new ArrayList<>();
        this.handledUserMessagesCounter = Counter.builder("handled_user_messages")
            .description("a number of handles telegram user messages")
            .register(meterRegistry);
    }

    @PostConstruct
    public void initializeCommandList() {
        List<Command> commands = new java.util.ArrayList<>(List.of(
            new StartCommand(botCommandService),
            new ListCommand(botCommandService),
            new TrackCommand(botCommandService),
            new UntrackCommand(botCommandService)
        ));
        HelpCommand helpCommand = new HelpCommand(commands.stream().map(Command::commandNameAndDescription).toList());
        commands.add(helpCommand);
        this.commandList = commands;
    }

    public List<String> commandDescriptionList() {
        return commandList.stream().map(Command::commandNameAndDescription).toList();
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
                handledUserMessagesCounter.increment();
                break;
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
