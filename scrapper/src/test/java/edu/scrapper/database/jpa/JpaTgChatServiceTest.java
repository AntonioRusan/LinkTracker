package edu.scrapper.database.jpa;

import edu.java.ScrapperApplication;
import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.services.tgChat.TgChatService;
import edu.scrapper.database.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {ScrapperApplication.class}, properties = {"app.database-access-type=jpa"})
public class JpaTgChatServiceTest extends IntegrationTest {
    @Autowired
    private TgChatService chatService;
    private static final Long CHAT_TEST_ID = 111L;

    @Test
    @Transactional
    @Rollback
    void addTgChatTest() {
        ResponseEntity<Void> chatResult = chatService.registerChat(CHAT_TEST_ID);
        assertThat(chatResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    @Test
    @Transactional
    @Rollback
    void addTgChatErrorTest() {
        ResponseEntity<Void> addResult = chatService.registerChat(CHAT_TEST_ID);
        assertThat(addResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
        assertThrows(ConflictException.class, () -> chatService.registerChat(CHAT_TEST_ID));
    }

    @Test
    @Transactional
    @Rollback
    void removeTgChatTest() {
        ResponseEntity<Void> addResult = chatService.registerChat(CHAT_TEST_ID);
        assertThat(addResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
        ResponseEntity<Void> deleteResult = chatService.unregisterChat(CHAT_TEST_ID);
        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    @Test
    @Transactional
    @Rollback
    void removeTgChatErrorTest() {
        assertThrows(NotFoundException.class, () -> chatService.unregisterChat(CHAT_TEST_ID));
    }
}
