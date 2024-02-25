package edu.scrapper.stackoverflow;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.stackoverflow.QuestionResponse;
import edu.java.stackoverflow.StackOverflowClient;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ScrapperApplication.class})
@WireMockTest
public class StackOverflowClientTest {

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("app.stackoverflow_base_url", wireMockExtension::baseUrl);
    }

    @Test
    public void testGetQuestionNotFound() {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/questions/1?site=stackoverflow"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(404)));
        QuestionResponse questionResponse =
            stackOverflowClient.getQuestion("https://stackoverflow.com/questions/1/test-question");
        assertThat(questionResponse).isNull();
    }

    @Test
    public void testGetQuestion() {
        OffsetDateTime updateTime = OffsetDateTime.parse("2024-02-27T12:00:00Z"); //1709035200
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/questions/1"))
            .withQueryParam("site", WireMock.equalTo("stackoverflow"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{\n" +
                        "  \"items\": [\n" +
                        "    {\n" +
                        "      \"is_answered\": true,\n" +
                        "      \"answer_count\": 2,\n" +
                        "      \"last_activity_date\": 1709035200,\n" +
                        "      \"creation_date\": 1256799465,\n" +
                        "      \"question_id\": 1,\n" +
                        "      \"link\": \"https://stackoverflow.com/questions/1/test-question\",\n" +
                        "      \"title\": \"Test question\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"
                )
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
