package edu.scrapper.bot;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.api.exceptions.base.BadRequestException;
import edu.java.clients.bot.BotClient;
import edu.java.clients.bot.models.LinkUpdate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ScrapperApplication.class})
@WireMockTest
public class BotClientTest {

    @Autowired
    private BotClient botWebClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("app.bot_api_url", wireMockExtension::baseUrl);
    }

    private static final String testUrl = "https://stackoverflow.com/questions/1/test-question";

    @Test
    public void testUpdates() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/updates"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)));
        LinkUpdate updateRequest = new LinkUpdate(
            1L,
            new URI(testUrl),
            "test question",
            List.of(1L)
        );
        assertDoesNotThrow(() -> botWebClient.sendUpdate(updateRequest));
    }

    @Test
    public void testUpdatesError() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/api/updates"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "description": "Некорректные параметры запроса"
                    }
                    """
                )
                .withStatus(400)));
        LinkUpdate updateRequest = new LinkUpdate(
            1L,
            new URI(testUrl),
            "test question",
            List.of(1L)
        );
        assertThrows(BadRequestException.class, () -> botWebClient.sendUpdate(updateRequest));
    }
}
