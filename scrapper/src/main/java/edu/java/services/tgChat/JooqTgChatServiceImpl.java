package edu.java.services.tgChat;

import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.models.Chat;
import edu.java.repositories.jooq.JooqChatLinkRepository;
import edu.java.repositories.jooq.JooqChatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static edu.java.exceptions.api.ApiError.TG_CHAT_ALREADY_REGISTERED;
import static edu.java.exceptions.api.ApiError.TG_CHAT_NOT_FOUND;

@Service
public class JooqTgChatServiceImpl implements TgChatService {
    private final JooqChatRepository chatRepository;
    private final JooqChatLinkRepository chatLinkRepository;

    public JooqTgChatServiceImpl(JooqChatRepository chatRepository, JooqChatLinkRepository chatLinkRepository) {
        this.chatRepository = chatRepository;
        this.chatLinkRepository = chatLinkRepository;
    }

    @Override
    public ResponseEntity<Void> unregisterChat(Long id) {
        if (chatRepository.findById(id).isPresent()) {
            chatLinkRepository.deleteByChatId(id);
            chatRepository.delete(id);
            return new ResponseEntity<>(
                HttpStatus.OK
            );
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Void> registerChat(Long id) {
        if (chatRepository.findById(id).isPresent()) {
            throw new ConflictException(TG_CHAT_ALREADY_REGISTERED);
        } else {
            chatRepository.add(new Chat(id));
            return new ResponseEntity<>(
                HttpStatus.OK
            );
        }
    }
}
