package edu.scrapper.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.clients.github.GitHubClientImpl;
import edu.java.clients.github.models.RepositoryResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import({GitHubTestConfiguration.class})
@ExtendWith(SpringExtension.class)
public class GitHubClientTest {

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
    public void testGetRepository() throws IOException {
        String okResponse = FileUtils.readFileToString(
            new File("src/test/resources/github/github_ok_response.json"),
            StandardCharsets.UTF_8
        );
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testOwner/testRepo"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(okResponse)
            )
        );

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
