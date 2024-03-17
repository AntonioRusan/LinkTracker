package edu.java.clients.github;

import edu.java.clients.github.models.RepositoryResponse;
import java.net.URI;

public interface GitHubClient {
    RepositoryResponse getRepository(URI url);
}
