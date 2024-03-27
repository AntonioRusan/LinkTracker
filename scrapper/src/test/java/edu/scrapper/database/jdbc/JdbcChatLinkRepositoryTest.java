package edu.scrapper.database.jdbc;

import edu.java.ScrapperApplication;
import edu.java.models.Chat;
import edu.java.models.Link;
import edu.java.repositories.jdbc.JdbcChatLinkRepository;
import edu.java.repositories.jdbc.JdbcChatRepository;
import edu.java.repositories.jdbc.JdbcLinkRepository;
import edu.scrapper.database.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JdbcChatLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatLinkRepository jdbcChatLinkRepository;

    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    private static final Long CHAT_TEST_ID = 111L;
    private static final String TEST_URL = "test.com";

    @Test
    @Transactional
    @Rollback
    void findAllLinksByChatIdTest() {
        Chat chat = new Chat(CHAT_TEST_ID);
        Link link = Link.create(TEST_URL);
        jdbcChatRepository.add(chat);
        Long linkId = jdbcLinkRepository.add(link);
        Integer updated = jdbcChatLinkRepository.add(CHAT_TEST_ID, linkId);
        assertThat(updated).isEqualTo(1);

        List<Link> linksFromDb = jdbcChatLinkRepository.findAllLinksByChatId(CHAT_TEST_ID);
        assertThat(linksFromDb.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatsByLinkIdTest() {
        Chat chat = new Chat(CHAT_TEST_ID);
        Link link = Link.create(TEST_URL);
        jdbcChatRepository.add(chat);

        Long linkId = jdbcLinkRepository.add(link);
        Integer updated = jdbcChatLinkRepository.add(CHAT_TEST_ID, linkId);
        assertThat(updated).isEqualTo(1);

        List<Chat> chatsFromDb = jdbcChatLinkRepository.findAllChatsByLinkId(linkId);
        assertThat(chatsFromDb.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        Chat chat = new Chat(CHAT_TEST_ID);
        Link link = Link.create(TEST_URL);
        jdbcChatRepository.add(chat);

        Long linkId = jdbcLinkRepository.add(link);
        Integer updated = jdbcChatLinkRepository.add(CHAT_TEST_ID, linkId);
        assertThat(updated).isEqualTo(1);

        Integer deleted = jdbcChatLinkRepository.delete(CHAT_TEST_ID, linkId);
        assertThat(deleted).isEqualTo(1);
    }
}
