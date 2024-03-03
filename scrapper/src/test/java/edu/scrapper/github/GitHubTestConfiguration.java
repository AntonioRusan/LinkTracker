package edu.scrapper.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.github.GitHubClient;
import edu.java.github.GitHubClientImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
public class GitHubTestConfiguration {
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
