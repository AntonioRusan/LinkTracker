package edu.scrapper.database.jdbc.service;

import edu.java.ScrapperApplication;
import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.services.tgChat.TgChatService;
import edu.scrapper.database.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest(classes = {ScrapperApplication.class}, properties = {"app.database-access-type=jdbc"})
public class JdbcTgChatServiceTest extends IntegrationTest {
    @Autowired
    private TgChatService chatService;
    private static final Long CHAT_TEST_ID = 111L;

    @Test
    @Transactional
    @Rollback
    void addTgChatTest() {
        assertDoesNotThrow(() -> chatService.registerChat(CHAT_TEST_ID));
    }

    @Test
    @Transactional
    @Rollback
    void addTgChatErrorTest() {
        assertDoesNotThrow(() -> chatService.registerChat(CHAT_TEST_ID));
        assertThrows(ConflictException.class, () -> chatService.registerChat(CHAT_TEST_ID));
    }

    @Test
    @Transactional
    @Rollback
    void removeTgChatTest() {
        assertDoesNotThrow(() -> chatService.registerChat(CHAT_TEST_ID));
        assertDoesNotThrow(() -> chatService.unregisterChat(CHAT_TEST_ID));
    }

    @Test
    @Transactional
    @Rollback
    void removeTgChatErrorTest() {
        assertThrows(NotFoundException.class, () -> chatService.unregisterChat(CHAT_TEST_ID));
    }
}
