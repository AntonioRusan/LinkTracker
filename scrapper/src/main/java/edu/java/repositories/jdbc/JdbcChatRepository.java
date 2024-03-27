package edu.java.repositories.jdbc;

import edu.java.models.Chat;
import edu.java.repositories.ChatRepositoryInterface;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcChatRepository implements ChatRepositoryInterface {
    private final JdbcClient jdbcClient;

    public JdbcChatRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM chat WHERE id = :id")
            .param("id", id)
            .query(Chat.class)
            .optional();
    }

    @Override
    public List<Chat> findAll() {
        return jdbcClient.sql("SELECT * FROM chat")
            .query(Chat.class)
            .list();
    }

    @Override
    public Integer add(Chat chat) {
        return jdbcClient.sql("INSERT INTO chat(id) values(:id)")
            .param("id", chat.id())
            .update();
    }

    @Override
    public Integer delete(Long id) {
        return jdbcClient.sql("DELETE FROM chat WHERE id = :id")
            .param("id", id)
            .update();
    }
}
