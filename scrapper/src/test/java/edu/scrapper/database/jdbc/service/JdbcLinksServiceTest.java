package edu.scrapper.database.jdbc.service;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import edu.java.ScrapperApplication;
import edu.java.models.GitHubLink;
import edu.java.models.StackOverflowLink;
import edu.java.services.links.LinksService;
import edu.java.services.tgChat.TgChatService;
import java.net.URI;
import java.util.Optional;
import edu.scrapper.database.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class}, properties = {"app.database-access-type=jdbc"})
public class JdbcLinksServiceTest extends IntegrationTest {
    @Autowired
    private LinksService linksService;
    @Autowired
    private TgChatService chatService;

    private static final Long CHAT_TEST_ID = 111L;
    private static final String GIT_HUB_TEST_URL = "https://github.com/AntonioRusan/LinkTracker/";
    private static final String STACKOVERFLOW_TEST_URL =
        "https://stackoverflow.com/questions/60414409/spring-jpa-composite-key-this-class-does-not-define-an-idclass";

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        assertDoesNotThrow(() -> chatService.registerChat(CHAT_TEST_ID));
        assertDoesNotThrow(() -> linksService.addLink(CHAT_TEST_ID, new AddLinkRequest(URI.create(GIT_HUB_TEST_URL))));
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinkTest() {
        assertDoesNotThrow(() -> chatService.registerChat(CHAT_TEST_ID));
        assertDoesNotThrow(() -> linksService.addLink(CHAT_TEST_ID, new AddLinkRequest(URI.create(GIT_HUB_TEST_URL))));
        ListLinksResponse allLinksResult = linksService.getAllLinks(CHAT_TEST_ID);
        assertThat(allLinksResult.getLinks().getFirst().getUrl().toString()).isEqualTo(GIT_HUB_TEST_URL);
    }

    @Test
    @Transactional
    @Rollback
    void addGitHubLinkTest() {
        chatService.registerChat(CHAT_TEST_ID);
        LinkResponse addLinkResult =
            linksService.addLink(CHAT_TEST_ID, new AddLinkRequest(URI.create(GIT_HUB_TEST_URL)));
        Optional<GitHubLink> gitHubResult = linksService.findGitHubByLinkId(addLinkResult.getId());
        assertTrue(gitHubResult.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void addStackOverflowLinkTest() {
        chatService.registerChat(CHAT_TEST_ID);
        LinkResponse addLinkResult =
            linksService.addLink(CHAT_TEST_ID, new AddLinkRequest(URI.create(STACKOVERFLOW_TEST_URL)));
        Optional<StackOverflowLink> stackOverflowResult =
            linksService.findStackOverflowByLinkId(addLinkResult.getId());
        assertTrue(stackOverflowResult.isPresent());
    }
}
