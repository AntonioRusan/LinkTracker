package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.services.bot_command.BotCommandService;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UntrackCommand implements Command {

    private final BotCommandService botCommandService;

    @Override
    public String commandName() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание изменений по ссылке <url>";
    }

    @Override
    public SendMessage handleCommand(Update update) {
        Long chatId = update.message().chat().id();
        String commandText = update.message().text();
        String url = commandText.substring(commandName().length()).trim();
        String response;
        if (isValidUrl(url)) {
            response = botCommandService.removeLink(chatId, url);
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
