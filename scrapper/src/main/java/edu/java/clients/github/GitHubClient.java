package edu.java.clients.github;

import edu.java.clients.github.models.RepositoryResponse;
import edu.java.clients.github.models.events.EventsResponse;
import java.net.URI;
import java.util.List;

public interface GitHubClient {
    RepositoryResponse getRepository(URI url);

    List<EventsResponse> getEvents(URI url);
}
