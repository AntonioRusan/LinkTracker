package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.UserRepository;
import java.util.List;

public class ListCommand implements Command {
    private UserRepository userRepository;

    public ListCommand() {
    }

    public ListCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        List<String> trackedUrls = userRepository.getUserLinks(chatId);
        String response = !trackedUrls.isEmpty() ? "Отслеживаемые ссылки:\n" + String.join("\n", trackedUrls)
            : "Нет отслеживаемых ссылок!";
        return new SendMessage(chatId, response);
    }
}
