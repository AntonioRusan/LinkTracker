package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.UserRepository;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class TrackCommand implements Command {
    private UserRepository userRepository;

    public TrackCommand() {
    }

    public TrackCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String commandName() {
        return "/track";
    }

    @Override
    public String description() {
        return "Отслеживать изменения по ссылке <url>";
    }

    @Override
    public SendMessage handleCommand(Update update) {
        Long chatId = update.message().chat().id();
        String commandText = update.message().text();
        String url = commandText.substring(commandName().length()).trim();
        String response;
        if (isValidUrl(url)) {
            response = userRepository.addLinkToUser(chatId, url);
        } else {
            response = "Введена неверная или пустая ссылка!";
        }
        return new SendMessage(chatId, response);
    }

    boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
