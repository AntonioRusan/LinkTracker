package edu.java.services.tgChat;

import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.models.jpa.ChatEntity;
import edu.java.models.jpa.LinkEntity;
import edu.java.repositories.jpa.JpaChatRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import static edu.java.exceptions.api.ApiError.TG_CHAT_ALREADY_REGISTERED;
import static edu.java.exceptions.api.ApiError.TG_CHAT_NOT_FOUND;

public class JpaTgChatServiceImpl implements TgChatService {
    private final JpaChatRepository chatRepository;

    public JpaTgChatServiceImpl(JpaChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public void unregisterChat(Long id) {
        Optional<ChatEntity> chatOpt = chatRepository.findById(id);
        if (chatOpt.isPresent()) {
            ChatEntity chat = chatOpt.get();
            Set<LinkEntity> links = chat.getLinks();
            links.forEach(link -> link.deleteChat(chat));
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void registerChat(Long id) {
        if (chatRepository.findById(id).isPresent()) {
            throw new ConflictException(TG_CHAT_ALREADY_REGISTERED);
        } else {
            chatRepository.save(new ChatEntity(id));
        }
    }
}
