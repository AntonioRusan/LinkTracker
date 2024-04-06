package edu.java.bot.services.updates_api;

import api.bot.models.LinkUpdate;
import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.handlers.LinkUpdatesHandler;
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
    private final LinkUpdatesHandler linkUpdatesHandler;
    private final static Logger LOGGER = LogManager.getLogger();

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        linkUpdatesHandler.handleLinkUpdate(linkUpdate);
        return ResponseEntity.ok().build();
    }
}
