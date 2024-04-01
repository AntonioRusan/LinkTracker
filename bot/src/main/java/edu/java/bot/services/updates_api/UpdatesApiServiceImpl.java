package edu.java.bot.services.updates_api;

import api.bot.models.LinkUpdate;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
@RequiredArgsConstructor
public class UpdatesApiServiceImpl implements UpdatesApiService {
    private final TelegramBot telegramBot;
    private final static Logger LOGGER = LogManager.getLogger();

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        linkUpdate.getTgChatIds().forEach(chatId -> {
            String link = linkUpdate.getUrl().toString();
            String message = String.format("Обновление ссылки %s:\n\n%s", link, linkUpdate.getDescription());
            telegramBot.execute(new SendMessage(chatId, message));
            LOGGER.info(String.format("Обновление ссылки %s пользователя %d", link, chatId));
        });
        return ResponseEntity.ok().build();
    }
}
