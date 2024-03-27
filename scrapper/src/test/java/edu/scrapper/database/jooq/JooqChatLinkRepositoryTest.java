package edu.scrapper.database.jooq;

import edu.java.ScrapperApplication;
import edu.java.models.Chat;
import edu.java.models.Link;
import edu.java.repositories.jooq.JooqChatLinkRepository;
import edu.java.repositories.jooq.JooqChatRepository;
import edu.java.repositories.jooq.JooqLinkRepository;
import edu.scrapper.database.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {ScrapperApplication.class}, properties = {"app.database-access-type=jooq"})
public class JooqChatLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatLinkRepository jooqChatLinkRepository;

    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Autowired
    private JooqLinkRepository jooqLinkRepository;
    private static final Long CHAT_TEST_ID = 111L;
    private static final String TEST_URL = "test.com";

    @Test
    @Transactional
    @Rollback
    void findAllLinksByChatIdTest() {
        Chat chat = new Chat(CHAT_TEST_ID);
        Link link = Link.create(TEST_URL);
        jooqChatRepository.add(chat);
        Long linkId = jooqLinkRepository.add(link);
        Integer updated = jooqChatLinkRepository.add(CHAT_TEST_ID, linkId);
        assertThat(updated).isEqualTo(1);

        List<Link> linksFromDb = jooqChatLinkRepository.findAllLinksByChatId(CHAT_TEST_ID);
        assertThat(linksFromDb.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatsByLinkIdTest() {
        Chat chat = new Chat(CHAT_TEST_ID);
        Link link = Link.create(TEST_URL);
        jooqChatRepository.add(chat);

        Long linkId = jooqLinkRepository.add(link);
        Integer updated = jooqChatLinkRepository.add(CHAT_TEST_ID, linkId);
        assertThat(updated).isEqualTo(1);

        List<Chat> chatsFromDb = jooqChatLinkRepository.findAllChatsByLinkId(linkId);
        assertThat(chatsFromDb.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        Chat chat = new Chat(CHAT_TEST_ID);
        Link link = Link.create(TEST_URL);
        jooqChatRepository.add(chat);

        Long linkId = jooqLinkRepository.add(link);
        Integer updated = jooqChatLinkRepository.add(CHAT_TEST_ID, linkId);
        assertThat(updated).isEqualTo(1);

        Integer deleted = jooqChatLinkRepository.delete(CHAT_TEST_ID, linkId);
        assertThat(deleted).isEqualTo(1);
    }
}
