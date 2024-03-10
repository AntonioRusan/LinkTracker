package edu.java.api.services.tgChat;

import edu.java.api.exceptions.base.ConflictException;
import edu.java.api.exceptions.base.NotFoundException;
import edu.java.repositories.ChatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static edu.java.api.exceptions.ApiError.TG_CHAT_ALREADY_REGISTERED;
import static edu.java.api.exceptions.ApiError.TG_CHAT_NOT_FOUND;

@Service
public class TgChatApiServiceImpl implements TgChatApiService {
    private final ChatRepository chatRepository;

    public TgChatApiServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public ResponseEntity<Void> tgChatIdDelete(Long id) {
        if (chatRepository.getChatById(id).isPresent()) {
            chatRepository.deleteChat(id);
            return new ResponseEntity<>(
                HttpStatus.OK
            );
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Void> tgChatIdPost(Long id) {
        if (chatRepository.getChatById(id).isPresent()) {
            throw new ConflictException(TG_CHAT_ALREADY_REGISTERED);
        } else {
            chatRepository.registerChat(id);
            return new ResponseEntity<>(
                HttpStatus.OK
            );
        }
    }
}
