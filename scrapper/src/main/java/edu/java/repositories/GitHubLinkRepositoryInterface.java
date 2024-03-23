package edu.java.repositories;

import edu.java.models.GitHubLink;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface GitHubLinkRepositoryInterface {
    Optional<GitHubLink> findByLinkId(Long linkId);

    Integer add(GitHubLink link);

    void updateLastPullRequestTime(Long linkId, OffsetDateTime lastPullRequestDate);
}
