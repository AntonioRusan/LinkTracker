package edu.bot.scrapper;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.BotApplication;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.model.AddLinkRequest;
import edu.java.bot.clients.scrapper.model.LinkResponse;
import edu.java.bot.clients.scrapper.model.ListLinksResponse;
import edu.java.bot.clients.scrapper.model.RemoveLinkRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BotApplication.class})
@WireMockTest
public class ScrapperClientTest {

    @Autowired
    private ScrapperClient scrapperClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("localhost", wireMockExtension::baseUrl);
    }

    @Test
    public void testAddLinkError() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/links"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        AddLinkRequest addLinkRequest = new AddLinkRequest(
            new URI("https://stackoverflow.com/questions/1/test-question")
        );
        LinkResponse response = scrapperClient.addLink(1L, addLinkRequest);

    }

    @Test
    public void testListLinksError() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/links"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        ListLinksResponse response = scrapperClient.listLinks(1L);
    }

    @Test
    public void testRemoveLinkError() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/api/links"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        RemoveLinkRequest request = new RemoveLinkRequest(
            new URI("https://stackoverflow.com/questions/1/test-question")
        );
        LinkResponse response = scrapperClient.deleteLink(1L, request);
    }

    @Test
    public void registerChatError() {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/tg-chat"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        scrapperClient.registerChat(1L);
    }

    @Test
    public void deleteChatError() {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/api/tg-chat"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        scrapperClient.deleteChat(1L);
    }

    @Test
    public void testAddLink() throws URISyntaxException {

        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/links"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));

        AddLinkRequest addLinkRequest = new AddLinkRequest(
            new URI("https://stackoverflow.com/questions/1/test-question")
        );
        LinkResponse response = scrapperClient.addLink(1L, addLinkRequest);

    }

    @Test
    public void testListLinks() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/links"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        ListLinksResponse response = scrapperClient.listLinks(1L);
    }

    @Test
    public void testRemoveLink() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/api/links"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        RemoveLinkRequest request = new RemoveLinkRequest(
            new URI("https://stackoverflow.com/questions/1/test-question")
        );
        LinkResponse response = scrapperClient.deleteLink(1L, request);
    }

    @Test
    public void registerChat() {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/tg-chat"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        scrapperClient.registerChat(1L);
    }

    @Test
    public void deleteChat() {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/api/tg-chat"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        scrapperClient.deleteChat(1L);
    }

    private String getJsonResponse(String filePath) {
        try {
            return FileUtils.readFileToString(
                new File(filePath),
                StandardCharsets.UTF_8
            );
        } catch (IOException ignored) {
            return "";
        }

    }

    @Test
    public void testGetQuestion() throws IOException {
        String okResponse = FileUtils.readFileToString(
            new File("src/test/resources/stackoverflow/stackoverflow_ok_response.json"),
            StandardCharsets.UTF_8
        );
        OffsetDateTime updateTime = OffsetDateTime.parse("2024-02-27T12:00:00Z"); //1709035200
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/questions/1"))
            .withQueryParam("site", WireMock.equalTo("stackoverflow"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(okResponse)
            )
        );

        QuestionResponse questionResponse =
            stackOverflowClient.getQuestion("https://stackoverflow.com/questions/1/test-question");
        QuestionResponse expectedResponse = new QuestionResponse(
            List.of(
                new QuestionResponse.ItemResponse(
                    1L,
                    "Test question",
                    "https://stackoverflow.com/questions/1/test-question",
                    updateTime
                )
            )
        );
        assertThat(questionResponse).isEqualTo(expectedResponse);
    }
}
