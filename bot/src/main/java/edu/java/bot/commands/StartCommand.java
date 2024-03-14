package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.models.User;
import edu.java.bot.repositories.UserRepository;

public class StartCommand implements Command {
    private UserRepository userRepository;

    public StartCommand() {
    }

    public StartCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        String repositoryResponse = userRepository.addUser(chatId, new User(chatId, firstName, lastName));
        String response = "Привет, " + firstName
            + " " + lastName + "!\n" + repositoryResponse;
        return new SendMessage(chatId, response);
    }
}
