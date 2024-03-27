package edu.java.data_fetchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.models.RepositoryResponse;
import edu.java.clients.github.models.events.EventsResponse;
import edu.java.clients.github.models.events.Payload;
import edu.java.data_fetchers.models.UpdateEvent;
import edu.java.data_fetchers.models.UpdateEventType;
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
        Optional<UpdateEvent> fetchedRepository = fetchRepositoryData(link);
        List<UpdateEvent> fetchedPullRequests = fetchPullRequestsData(link);
        List<String> description = new ArrayList<>();
        fetchedRepository.ifPresent(repository -> description.add(repository.toString()));
        if (!fetchedPullRequests.isEmpty()) {
            List<String> pullRequestsDescription =
                fetchedPullRequests.stream().map(UpdateEvent::toString).toList();
            description.add("Обновление Pull Requests:\n\n" + String.join("\n", pullRequestsDescription));
        }
        linksService.updateLinkCheckTime(link.id(), OffsetDateTime.now());
        return String.join("\n\n", description);
    }

    public Optional<UpdateEvent> fetchRepositoryData(Link link) {
        URI url = URI.create(link.url());
        RepositoryResponse repository = gitHubClient.getRepository(url);
        if (repository != null && repository.updatedAt().isAfter(link.updatedAt())) {
            linksService.updateLinkCheckAndUpdatedTime(
                link.id(),
                OffsetDateTime.now(),
                repository.updatedAt()
            );
            return Optional.of(new UpdateEvent(
                UpdateEventType.REPOSITORY_UPDATE,
                repository.name(),
                repository.htmlUrl(),
                repository.updatedAt()
            ));
        } else {
            return Optional.empty();
        }
    }

    public List<UpdateEvent> fetchPullRequestsData(Link link) {
        URI url = URI.create(link.url());
        List<EventsResponse> events = gitHubClient.getEvents(url);

        ObjectMapper jsonMapper = new ObjectMapper();
        List<UpdateEvent> pullRequests = new ArrayList<>();

        linksService.findGitHubByLinkId(link.id()).ifPresent(gitHubLink -> {
            events.reversed().forEach(event -> {
                try {
                    Payload payload = jsonMapper.treeToValue(event.payload(), Payload.class);
                    if (event.createdAt().isAfter(gitHubLink.lastPullRequestDate())) {
                        pullRequests.add(new UpdateEvent(
                            UpdateEventType.NEW_PULL_REQUEST,
                            payload.pullRequestEvent().title(),
                            payload.pullRequestEvent().htmlUrl(),
                            event.createdAt()
                        ));
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            pullRequests.stream().max(Comparator.comparing(UpdateEvent::createdAt)).ifPresent(
                lastPR -> linksService.updateGitHubLinkLastPullRequestDate(gitHubLink.linkId(), lastPR.createdAt())
            );
        });
        return pullRequests;
    }
}
