package edu.java.github;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class GitHubClientImpl implements GitHubClient {
    private final WebClient webClient;
    private final static Logger LOGGER = LogManager.getLogger();

    @Autowired
    public GitHubClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public RepositoryResponse getRepositoryResponse(String owner, String repo) {
        return webClient
            .get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .doOnError(error -> LOGGER.error("An error has occurred {}", error.getMessage()))
            .block();
    }

    @Override
    public RepositoryResponse getRepository(String url) {
        try {
            Pair<String, String> ownerAndRepo = getOwnerAndRepoFromUrl(url);
            return getRepositoryResponse(ownerAndRepo.getValue0(), ownerAndRepo.getValue1());
        } catch (Exception ex) {
            LOGGER.error("Не удалось отследить ссылку: " + ex.getMessage());
            return null;
        }
    }

    private Pair<String, String> getOwnerAndRepoFromUrl(String inputUrl) {
        try {
            URL url = new URI(inputUrl).toURL();
            String[] pathParts =
                Arrays.stream(url.getPath().split("/")).filter(path -> !path.isEmpty()).toArray(String[]::new);
            if (pathParts.length == 2) {
                String owner = pathParts[0];
                String repo = pathParts[1];
                return new Pair<>(owner, repo);
            } else {
                throw new RuntimeException();
            }
        } catch (MalformedURLException | URISyntaxException ex) {
            throw new RuntimeException("Неправильная ссылка на GitHub");
        }
    }

    /*@RequestMapping("/github")
    public RepositoryResponse hello() {
        try {

            return getRepositoryResponse("AntonioRusan", "LinkTracker");
        } catch (Exception ex) {
            LOGGER.error("error");
            return null;
        }
    }*/
}
