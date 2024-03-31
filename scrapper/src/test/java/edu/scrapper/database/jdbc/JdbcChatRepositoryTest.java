package edu.scrapper.database.jdbc;

import edu.java.ScrapperApplication;
import edu.java.models.Chat;
import edu.java.repositories.jdbc.JdbcChatRepository;
import edu.scrapper.database.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class}, properties = {"app.database-access-type=jdbc"})
public class JdbcChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    private static final Long TEST_ID = 111L;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Chat chat = new Chat(TEST_ID);
        Integer updated = jdbcChatRepository.add(chat);
        assertThat(updated).isEqualTo(1);
        jdbcChatRepository.delete(TEST_ID);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {
        Chat chat = new Chat(TEST_ID);
        Integer updated = jdbcChatRepository.add(chat);
        assertThat(updated).isEqualTo(1);

        Optional<Chat> chatFromDb = jdbcChatRepository.findById(TEST_ID);
        assertTrue(chatFromDb.isPresent());
        assertThat(chatFromDb.get().id()).isEqualTo(TEST_ID);
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        Chat chat = new Chat(TEST_ID);
        Integer updated = jdbcChatRepository.add(chat);
        Integer deleted = jdbcChatRepository.delete(TEST_ID);
        assertThat(deleted).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Chat chat = new Chat(TEST_ID);
        jdbcChatRepository.add(chat);
        chat = new Chat(TEST_ID + 1);
        jdbcChatRepository.add(chat);

        List<Chat> chatsFromDb = jdbcChatRepository.findAll();
        assertThat(chatsFromDb.size()).isEqualTo(2);
    }
}
