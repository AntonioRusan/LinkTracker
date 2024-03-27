package edu.java.data_fetchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.models.RepositoryResponse;
import edu.java.clients.github.models.events.EventsResponse;
import edu.java.clients.github.models.events.Payload;
import edu.java.data_fetchers.models.github.PullRequestUpdate;
import edu.java.data_fetchers.models.github.RepositoryUpdate;
import edu.java.models.Link;
import edu.java.services.links.LinksService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class GithubDataFetcher {
    private final GitHubClient gitHubClient;
    private final LinksService linksService;
    private final static Logger LOGGER = LogManager.getLogger();

    public GithubDataFetcher(GitHubClient gitHubClient, LinksService linksService) {
        this.gitHubClient = gitHubClient;
        this.linksService = linksService;
    }

    public String fetchData(Link link) {
        Optional<RepositoryUpdate> fetchedRepository = fetchRepositoryData(link);
        List<PullRequestUpdate> fetchedPullRequests = fetchPullRequestsData(link);
        List<String> description = new ArrayList<>();
        fetchedRepository.ifPresent(repository -> description.add(repository.toString()));
        if (!fetchedPullRequests.isEmpty()) {
            List<String> pullRequestsDescription =
                fetchedPullRequests.stream().map(PullRequestUpdate::toString).toList();
            description.add("Обновление Pull Requests:\n\n" + String.join("\n", pullRequestsDescription));
        }
        linksService.updateLinkCheckTime(link.id(), OffsetDateTime.now());
        return String.join("\n\n", description);
    }

    public Optional<RepositoryUpdate> fetchRepositoryData(Link link) {
        URI url = URI.create(link.url());
        RepositoryResponse repository = gitHubClient.getRepository(url);
        if (repository != null && repository.updatedAt().isAfter(link.updatedAt())) {
            linksService.updateLinkCheckAndUpdatedTime(
                link.id(),
                OffsetDateTime.now(),
                repository.updatedAt()
            );
            return Optional.of(new RepositoryUpdate(repository.name(), repository.htmlUrl(), repository.updatedAt()));
        } else {
            return Optional.empty();
        }
    }

    public List<PullRequestUpdate> fetchPullRequestsData(Link link) {
        URI url = URI.create(link.url());
        List<EventsResponse> events = gitHubClient.getEvents(url);

        ObjectMapper jsonMapper = new ObjectMapper();
        List<PullRequestUpdate> pullRequests = new ArrayList<>();

        linksService.findGitHubByLinkId(link.id()).ifPresent(gitHubLink -> {
            events.reversed().forEach(event -> {
                try {
                    Payload payload = jsonMapper.treeToValue(event.payload(), Payload.class);
                    if (event.createdAt().isAfter(gitHubLink.lastPullRequestDate())) {
                        pullRequests.add(new PullRequestUpdate(
                            payload.pullRequestEvent().title(),
                            event.createdAt(),
                            payload.pullRequestEvent().htmlUrl()
                        ));
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            pullRequests.stream().max(Comparator.comparing(PullRequestUpdate::createdAt)).ifPresent(
                lastPR -> linksService.updateGitHubLinkLastPullRequestDate(gitHubLink.linkId(), lastPR.createdAt())
            );
        });
        return pullRequests;
    }
}
