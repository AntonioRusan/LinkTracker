package edu.java.clients.github;

import edu.java.clients.github.models.RepositoryResponse;
import edu.java.clients.github.models.events.EventsResponse;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@SuppressWarnings("MultipleStringLiterals")
public class GitHubClientImpl implements GitHubClient {

    private final WebClient webClient;
    private final static Logger LOGGER = LogManager.getLogger();

    @Autowired
    public GitHubClientImpl(@Qualifier("gitHubWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    private RepositoryResponse getRepositoryResponse(String owner, String repo) {
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
    public RepositoryResponse getRepository(URI url) {
        try {
            Pair<String, String> ownerAndRepo = getOwnerAndRepoFromUrl(url);
            return getRepositoryResponse(ownerAndRepo.getValue0(), ownerAndRepo.getValue1());
        } catch (Exception ex) {
            LOGGER.error("Не удалось отследить ссылку: " + ex.getMessage());
            return null;
        }
    }

    private EventsResponse[] getEventsResponse(String owner, String repo) {
        return webClient
            .get()
            .uri("/repos/{owner}/{repo}/events", owner, repo)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(EventsResponse[].class)
            .doOnError(error -> LOGGER.error("An error has occurred {}", error.getMessage()))
            .block();
    }

    @Override
    public List<EventsResponse> getEvents(URI url) {
        try {
            Pair<String, String> ownerAndRepo = getOwnerAndRepoFromUrl(url);
            var gg = Arrays.stream(getEventsResponse(ownerAndRepo.getValue0(), ownerAndRepo.getValue1()));
            return gg.filter(item -> item.type().equals("PullRequestEvent")).toList();
        } catch (Exception ex) {
            LOGGER.error("Не удалось отследить ссылку: " + ex.getMessage());
            return null;
        }
    }

    private Pair<String, String> getOwnerAndRepoFromUrl(URI inputUrl) {
        try {
            URL url = inputUrl.toURL();
            String path = url.getPath();
            Pattern patternPath = Pattern.compile("/(\\w+)/(\\w+)");
            Matcher matcherPath = patternPath.matcher(path);
            if (matcherPath.find()) {
                String owner = matcherPath.group(1);
                String repo = matcherPath.group(2);
                return new Pair<>(owner, repo);
            } else {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Неправильная ссылка на GitHub");
        }
    }
}
