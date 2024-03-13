package edu.scrapper.bot;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.clients.bot.BotClient;
import edu.java.clients.bot.models.LinkUpdate;
import edu.java.stackoverflow.QuestionResponse;
import edu.java.stackoverflow.StackOverflowClient;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        registry.add("localhost", wireMockExtension::baseUrl);
    }

    @Test
    public void testUpdates() throws URISyntaxException {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/updates"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)));
        LinkUpdate updateRequest = new LinkUpdate(
            1L,
            new URI("https://stackoverflow.com/questions/1/test-question"),
            "test question",
            List.of(1L)
        );
        botWebClient.sendUpdate(updateRequest);
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
