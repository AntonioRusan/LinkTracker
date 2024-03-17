package edu.java;

import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.models.RepositoryResponse;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.models.QuestionResponse;
import java.net.URI;
import org.springframework.stereotype.Service;

@Service
public class DataFetcher {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    public DataFetcher(GitHubClient gitHubClient, StackOverflowClient stackOverflowClient) {
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
    }

    public FetchedLinkData fetchData(URI url) {
        String host = url.getHost();
        return switch (host) {
            case "github.com" -> {
                RepositoryResponse repository = gitHubClient.getRepository(url);
                yield new FetchedLinkData(
                    URI.create(repository.htmlUrl()),
                    repository.updatedAt(),
                    repository.id(),
                    repository.name()
                );
            }
            case "stackoverflow.com" -> {
                QuestionResponse questionResponse = stackOverflowClient.getQuestion(url);
                if (!questionResponse.items().isEmpty()) {
                    QuestionResponse.ItemResponse item = questionResponse.items().getFirst();
                    yield new FetchedLinkData(
                        URI.create(item.link()),
                        item.lastActivityDate(),
                        item.questionId(),
                        item.title()
                    );
                } else {
                    yield null;
                }
            }
            default -> null;
        };
    }
}
