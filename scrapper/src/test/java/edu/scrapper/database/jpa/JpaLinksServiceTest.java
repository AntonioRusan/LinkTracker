package edu.scrapper.database.jpa;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import edu.java.ScrapperApplication;
import edu.java.models.GitHubLink;
import edu.java.models.StackOverflowLink;
import edu.java.services.links.JpaLinksServiceImpl;
import edu.java.services.tgChat.JpaTgChatServiceImpl;
import edu.scrapper.database.IntegrationTest;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JpaLinksServiceTest extends IntegrationTest {
    @Autowired
    private JpaLinksServiceImpl jpaLinksService;
    @Autowired
    private JpaTgChatServiceImpl jpaTgChatService;

    private static final Long CHAT_TEST_ID = 111L;
    private static final String GIT_HUB_TEST_URL = "https://github.com/AntonioRusan/LinkTracker/";
    private static final String STACKOVERFLOW_TEST_URL =
        "https://stackoverflow.com/questions/60414409/spring-jpa-composite-key-this-class-does-not-define-an-idclass";

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        ResponseEntity<Void> chatResult = jpaTgChatService.registerChat(CHAT_TEST_ID);
        assertThat(chatResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        ResponseEntity<LinkResponse> linkResult =
            jpaLinksService.addLink(CHAT_TEST_ID, new AddLinkRequest(URI.create(GIT_HUB_TEST_URL)));
        assertThat(linkResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinkTest() {
        ResponseEntity<Void> chatResult = jpaTgChatService.registerChat(CHAT_TEST_ID);
        assertThat(chatResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        ResponseEntity<LinkResponse> linkResult =
            jpaLinksService.addLink(CHAT_TEST_ID, new AddLinkRequest(URI.create(GIT_HUB_TEST_URL)));
        assertThat(linkResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        ResponseEntity<ListLinksResponse> allLinksResult = jpaLinksService.getAllLinks(CHAT_TEST_ID);
        assertThat(allLinksResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(allLinksResult.getBody().getLinks().getFirst().getUrl().toString()).isEqualTo(GIT_HUB_TEST_URL);
    }

    @Test
    @Transactional
    @Rollback
    void addGitHubLinkTest() {
        ResponseEntity<Void> chatResult = jpaTgChatService.registerChat(CHAT_TEST_ID);
        ResponseEntity<LinkResponse> addLinkResult =
            jpaLinksService.addLink(CHAT_TEST_ID, new AddLinkRequest(URI.create(GIT_HUB_TEST_URL)));
        Optional<GitHubLink> gitHubResult = jpaLinksService.findGitHubByLinkId(addLinkResult.getBody().getId());
        assertTrue(gitHubResult.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void addStackOverflowLinkTest() {
        ResponseEntity<Void> chatResult = jpaTgChatService.registerChat(CHAT_TEST_ID);
        ResponseEntity<LinkResponse> addLinkResult =
            jpaLinksService.addLink(CHAT_TEST_ID, new AddLinkRequest(URI.create(STACKOVERFLOW_TEST_URL)));
        Optional<StackOverflowLink> stackOverflowResult =
            jpaLinksService.findStackOverflowByLinkId(addLinkResult.getBody().getId());
        assertTrue(stackOverflowResult.isPresent());
    }
}
