package edu.java.repositories;

import edu.java.models.Chat;
import edu.java.models.Link;
import java.util.List;

public interface ChatLinkRepositoryInterface {
    List<Link> findAllLinksByChatId(Long chatId);

    List<Chat> findAllChatsByLinkId(Long linkId);

    Integer add(Long chatId, Long linkId);

    Integer delete(Long chatId, Long linkId);

    Integer deleteByChatId(Long chatId);
}
