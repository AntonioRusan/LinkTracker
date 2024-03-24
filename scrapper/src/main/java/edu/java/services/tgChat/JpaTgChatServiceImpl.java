package edu.java.services.tgChat;

import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.models.jpa.ChatEntity;
import edu.java.models.jpa.LinkEntity;
import edu.java.repositories.jpa.JpaChatRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static edu.java.exceptions.api.ApiError.TG_CHAT_ALREADY_REGISTERED;
import static edu.java.exceptions.api.ApiError.TG_CHAT_NOT_FOUND;

@Service
public class JpaTgChatServiceImpl implements TgChatService {
    private final JpaChatRepository chatRepository;

    public JpaTgChatServiceImpl(JpaChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public ResponseEntity<Void> unregisterChat(Long id) {
        Optional<ChatEntity> chatOpt = chatRepository.findById(id);
        if (chatOpt.isPresent()) {
            ChatEntity chat = chatOpt.get();
            Set<LinkEntity> links = chat.getLinks();
            links.forEach(link -> link.deleteChat(chat));
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
            chatRepository.save(new ChatEntity(id));
            return new ResponseEntity<>(
                HttpStatus.OK
            );
        }
    }
}
