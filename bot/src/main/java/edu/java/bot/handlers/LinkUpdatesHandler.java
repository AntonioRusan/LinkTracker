package edu.java.bot.handlers;

import api.bot.models.LinkUpdate;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LinkUpdatesHandler {
    private final TelegramBot telegramBot;
    private final static Logger LOGGER = LogManager.getLogger();

    public LinkUpdatesHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void handleLinkUpdate(LinkUpdate linkUpdate) {
        linkUpdate.getTgChatIds().forEach(chatId -> {
            String link = linkUpdate.getUrl().toString();
            String message = String.format("Обновление ссылки %s:\n\n%s", link, linkUpdate.getDescription());
            telegramBot.execute(new SendMessage(chatId, message));
            LOGGER.info(String.format("Обновление ссылки %s пользователя %d", link, chatId));
        });
    }
}
