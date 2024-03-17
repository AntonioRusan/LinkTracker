package edu.java.bot.services.updates_api;

import api.bot.models.LinkUpdate;
import edu.java.bot.exceptions.api.ApiError;
import edu.java.bot.exceptions.api.base.BadRequestException;
import edu.java.bot.models.User;
import edu.java.bot.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
public class UpdatesApiServiceImpl implements UpdatesApiService {
    private final UserRepository userRepository;
    private final static Logger LOGGER = LogManager.getLogger();

    public UpdatesApiServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        linkUpdate.getTgChatIds().forEach(chatId -> {
            //Optional<User> userOpt = userRepository.getUser(chatId);
            Optional<User> userOpt = Optional.of(new User(111L, "test", "test"));
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.addLink("https://github.com/AntonioRusan/LinkTracker");
                List<String> userLinks = userOpt.get().getLinkList();
                String link = linkUpdate.getUrl().toString();
                if (userLinks.contains(link)) {
                    LOGGER.info(String.format("Обновление ссылки %s пользователя %d", link, chatId));
                } else {
                    throw new BadRequestException(ApiError.LINK_NOT_FOUND);
                }
            } else {
                throw new BadRequestException(ApiError.CHATS_NOT_FOUND);
            }
        });
        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }
}
