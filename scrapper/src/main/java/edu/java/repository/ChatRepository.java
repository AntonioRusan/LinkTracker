package edu.java.repository;

import edu.java.model.Chat;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRepository {
    public Optional<Chat> getChatById(Long chatId) {
        return Optional.of(new Chat(1L));
    }

    public void registerChat(Long chatId) {

    }

    public void deleteChat(Long chatId) {

    }
}
