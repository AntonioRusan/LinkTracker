package edu.java.repositories;

import edu.java.models.Chat;
import edu.java.models.Link;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class JdbcChatLinkRepository implements ChatLinkRepositoryInterface {
    private final JdbcClient jdbcClient;

    public JdbcChatLinkRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Link> findAllLinksByChatId(Long chatId) {
        return jdbcClient.sql("""
                SELECT l.*
                FROM chat_link
                INNER JOIN link l
                ON link.id = chat_link.link_id
                WHERE chat_link.chat_id = :chatId
                """)
            .param("chatId", chatId)
            .query(Link.class)
            .list();
    }

    @Override
    public List<Chat> findAllChatsByLinkId(Long linkId) {
        return jdbcClient.sql("""
                SELECT c.*
                FROM chat_link
                INNER JOIN chat c
                ON chat.id = chat_link.chat_id
                WHERE chat_link.link_id = :linkId
                """)
            .param("linkId", linkId)
            .query(Chat.class)
            .list();
    }

    @Override
    public Integer add(Long chatId, Long linkId) {
        return jdbcClient.sql("INSERT INTO chat_link(chat_id, link_id) values(:chatId, :linkId)")
            .param("chatId", chatId)
            .param("linkId", linkId)
            .update();
    }

    @Override
    public Integer delete(Long chatId, Long linkId) {
        return jdbcClient.sql("DELETE FROM chat WHERE chat_id = :chatId AND link_id = :linkId")
            .param("chatId", chatId)
            .param("linkId", linkId)
            .update();
    }

    @Override
    public Integer deleteByChatId(Long chatId) {
        return jdbcClient.sql("DELETE FROM chat WHERE chat_id = :chatId")
            .param("chatId", chatId)
            .update();
    }
}
