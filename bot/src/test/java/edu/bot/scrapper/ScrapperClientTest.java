package edu.bot.scrapper;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import api.scrapper.models.RemoveLinkRequest;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.BotApplication;
import edu.java.bot.exceptions.api.base.BadRequestException;
import edu.java.bot.exceptions.api.base.ConflictException;
import edu.java.bot.exceptions.api.base.NotFoundException;
import edu.java.bot.clients.scrapper.ScrapperClient;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        registry.add("app.scrapper_api_url", wireMockExtension::baseUrl);
    }

    private static final String testUrl = "https://stackoverflow.com/questions/1/test-question";
    private static final String requestsDir = "src/test/resources/scrapper/requests/";
    private static final String responsesDir = "src/test/resources/scrapper/responses/";
    private final static Logger LOGGER = LogManager.getLogger();

    @Test
    public void testAddLink() throws URISyntaxException {
        String okResponse = getJsonResponseFromFile(responsesDir + "remove_link_ok.json");
        String requestJson = getJsonResponseFromFile(requestsDir + "remove_link_request.json");
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/links"))
            .withHeader("id", equalTo("1"))
            .withRequestBody(equalToJson(requestJson))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(okResponse)
            )
        );
        AddLinkRequest addLinkRequest = new AddLinkRequest(
            new URI(testUrl)
        );
        LinkResponse response = scrapperClient.addLink(1L, addLinkRequest);
        LinkResponse expectedResponse = new LinkResponse(1L, new URI(testUrl));
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void testAddLinkError() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/links"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "description": "Повторное добавление ссылки"
                    }
                    """
                )
                .withStatus(409)));
        AddLinkRequest addLinkRequest = new AddLinkRequest(
            new URI(testUrl)
        );
        assertThrows(ConflictException.class, () -> scrapperClient.addLink(1L, addLinkRequest));
    }

    @Test
    public void testListLinks() throws URISyntaxException {
        String okResponse = getJsonResponseFromFile(responsesDir + "list_links_ok.json");
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/links"))
            .withHeader("id", equalTo("1"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(okResponse)
            )
        );
        ListLinksResponse response = scrapperClient.listLinks(1L);

        ListLinksResponse expectedResponse = new ListLinksResponse(
            List.of(new LinkResponse(1L, new URI(testUrl)))
        );
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void testListLinksError() {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/links"))
            .withHeader("id", equalTo("1"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "description": "Некорректные параметры запроса"
                    }
                    """
                )
                .withStatus(400)));
        assertThrows(BadRequestException.class, () -> scrapperClient.listLinks(1L));
    }

    @Test
    public void testRemoveLink() throws URISyntaxException {
        String okResponse = getJsonResponseFromFile(responsesDir + "remove_link_ok.json");
        String requestJson = getJsonResponseFromFile(requestsDir + "remove_link_request.json");
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/api/links"))
            .withHeader("id", equalTo("1"))
            .withRequestBody(equalToJson(requestJson))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(okResponse)
            )
        );
        RemoveLinkRequest request = new RemoveLinkRequest(
            new URI(testUrl)
        );
        LinkResponse response = scrapperClient.deleteLink(1L, request);
        LinkResponse expectedResponse = new LinkResponse(1L, new URI(testUrl));
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void testRemoveLinkError() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/api/links"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "description": "Ссылка не найдена"
                    }
                    """
                )
                .withStatus(404)));
        RemoveLinkRequest request = new RemoveLinkRequest(
            new URI(testUrl)
        );
        assertThrows(NotFoundException.class, () -> scrapperClient.deleteLink(1L, request));
    }

    @Test
    public void registerChat() {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/tg-chat/1"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
            )
        );
        assertDoesNotThrow(() -> scrapperClient.registerChat(1L));
    }

    @Test
    public void registerChatError() {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/tg-chat/1"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "description": "Некорректные параметры запроса"
                    }
                    """
                )
                .withStatus(409)));
        assertThrows(ConflictException.class, () -> scrapperClient.registerChat(1L));
    }

    @Test
    public void deleteChat() {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/api/tg-chat/1"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
            )
        );
        assertDoesNotThrow(() -> scrapperClient.deleteChat(1L));
    }

    @Test
    public void deleteChatError() {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/api/tg-chat/1"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "description": "Чат не найден"
                    }
                    """
                )
                .withStatus(404)));
        assertThrows(NotFoundException.class, () -> scrapperClient.deleteChat(1L));
    }

    private String getJsonResponseFromFile(String filePath) {
        try {
            return FileUtils.readFileToString(
                new File(filePath),
                StandardCharsets.UTF_8
            );
        } catch (IOException ignored) {
            LOGGER.error("Error reading json from file: " + filePath);
            return "";
        }

    }
}
