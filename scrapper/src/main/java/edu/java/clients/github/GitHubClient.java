package edu.java.clients.github;

import edu.java.clients.github.models.RepositoryResponse;

public interface GitHubClient {
    RepositoryResponse getRepositoryResponse(String owner, String repo);

    RepositoryResponse getRepository(String url);
}
