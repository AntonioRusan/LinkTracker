package edu.java.github;

public interface GitHubClient {
    RepositoryResponse getRepositoryResponse(String owner, String repo);

    RepositoryResponse getRepository(String url);
}
