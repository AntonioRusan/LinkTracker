package edu.java.repositories.jdbc;

import edu.java.models.Chat;
import edu.java.models.Link;
import edu.java.repositories.ChatLinkRepositoryInterface;
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
                FROM chat_link cl
                INNER JOIN link l
                ON l.id = cl.link_id
                WHERE cl.chat_id = :chatId
                """)
            .param("chatId", chatId)
            .query(Link.class)
            .list();
    }

    @Override
    public List<Chat> findAllChatsByLinkId(Long linkId) {
        return jdbcClient.sql("""
                SELECT c.*
                FROM chat_link cl
                INNER JOIN chat c
                ON c.id = cl.chat_id
                WHERE cl.link_id = :linkId
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
        return jdbcClient.sql("DELETE FROM chat_link WHERE chat_id = :chatId AND link_id = :linkId")
            .param("chatId", chatId)
            .param("linkId", linkId)
            .update();
    }

    @Override
    public Integer deleteByChatId(Long chatId) {
        return jdbcClient.sql("DELETE FROM chat_link WHERE chat_id = :chatId")
            .param("chatId", chatId)
            .update();
    }
}
