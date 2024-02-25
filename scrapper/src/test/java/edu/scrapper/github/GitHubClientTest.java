package edu.scrapper.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.github.GitHubClient;
import edu.java.github.GitHubClientImpl;
import edu.java.github.RepositoryResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class GitHubClientTest {
    @TestConfiguration
    static class TestConfig {
        @Bean
        public WireMockServer wireMockServer() {
            WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());
            wireMockServer.start();
            return wireMockServer;
        }

        @Bean
        public WebClient webClient(WireMockServer server) {
            return WebClient.builder().baseUrl(server.baseUrl()).build();
        }

        @Bean
        public GitHubClient gitHubClient(WebClient webClient) {
            return new GitHubClientImpl(webClient);
        }
    }

    @Autowired
    private WireMockServer wireMockServer;
    @Autowired
    private GitHubClientImpl gitHubClient;

    @Test
    public void testGetRepositoryNotFound() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testOwner/testRepo"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(404)));
        RepositoryResponse repositoryResponse = gitHubClient.getRepository("https://github.com/testOwner/testRepo");
        assertThat(repositoryResponse).isNull();
    }

    @Test
    public void testGetRepository() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testOwner/testRepo"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{\"id\": \"1\", " +
                        "\"name\": \"testRepo\", " +
                        "\"description\": \"Test repository\", " +
                        "\"html_url\": \"https://github.com/testOwner/testRepo\", " +
                        "\"updated_at\": \"2024-02-27T12:00:00Z\"}"
                )));

        RepositoryResponse repositoryResponse = gitHubClient.getRepository("https://github.com/testOwner/testRepo");
        RepositoryResponse expectedResponse = new RepositoryResponse(
            1L,
            "testRepo",
            "https://github.com/testOwner/testRepo",
            OffsetDateTime.parse("2024-02-27T12:00:00Z")
        );
        assertThat(repositoryResponse).isEqualTo(expectedResponse);
    }
}
