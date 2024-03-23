package edu.java.data_fetchers;

import edu.java.models.Link;
import java.net.URI;
import org.springframework.stereotype.Service;

@Service
public class DataFetcher {
    private final GithubDataFetcher githubDataFetcher;
    private final StackOverflowDataFetcher stackOverflowDataFetcher;

    public DataFetcher(
        GithubDataFetcher githubDataFetcher,
        StackOverflowDataFetcher stackOverflowDataFetcher
    ) {
        this.githubDataFetcher = githubDataFetcher;
        this.stackOverflowDataFetcher = stackOverflowDataFetcher;
    }

    public String fetchData(Link link) {
        URI uri = URI.create(link.url());
        String host = uri.getHost();
        return switch (host) {
            case "github.com" -> githubDataFetcher.fetchData(link);
            case "stackoverflow.com" -> stackOverflowDataFetcher.fetchData(link);
            default -> "";
        };
    }
}
