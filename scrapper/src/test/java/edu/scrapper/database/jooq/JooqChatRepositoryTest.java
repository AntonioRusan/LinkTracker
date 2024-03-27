package edu.scrapper.database.jooq;

import edu.java.ScrapperApplication;
import edu.java.models.Chat;
import edu.java.repositories.jooq.JooqChatRepository;
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

@SpringBootTest(classes = {ScrapperApplication.class})
public class JooqChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository jooqChatRepository;

    private static final Long TEST_ID = 111L;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Chat chat = new Chat(TEST_ID);
        Integer updated = jooqChatRepository.add(chat);
        assertThat(updated).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {
        Chat chat = new Chat(TEST_ID);
        Integer updated = jooqChatRepository.add(chat);
        assertThat(updated).isEqualTo(1);

        Optional<Chat> chatFromDb = jooqChatRepository.findById(TEST_ID);
        assertTrue(chatFromDb.isPresent());
        assertThat(chatFromDb.get().id()).isEqualTo(TEST_ID);
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        Chat chat = new Chat(TEST_ID);
        Integer updated = jooqChatRepository.add(chat);
        Integer deleted = jooqChatRepository.delete(TEST_ID);
        assertThat(deleted).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Chat chat = new Chat(TEST_ID);
        jooqChatRepository.add(chat);
        chat = new Chat(TEST_ID + 1);
        jooqChatRepository.add(chat);

        List<Chat> chatsFromDb = jooqChatRepository.findAll();
        assertThat(chatsFromDb.size()).isEqualTo(2);
    }
}
