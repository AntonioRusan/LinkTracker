package edu.java.repositories.jdbc;

import edu.java.models.GitHubLink;
import edu.java.repositories.GitHubLinkRepositoryInterface;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class JdbcGitHubLinkRepository implements GitHubLinkRepositoryInterface {
    private final JdbcClient jdbcClient;

    public JdbcGitHubLinkRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<GitHubLink> findByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT * FROM github_link WHERE link_id = :linkId")
            .param("linkId", linkId)
            .query(GitHubLink.class)
            .optional();
    }

    @Override
    public Integer add(GitHubLink link) {
        return jdbcClient.sql(
                "INSERT INTO github_link(link_id, last_pull_request_date) VALUES (:linkId, :lastPullRequestDate)"
            )
            .param("linkId", link.linkId())
            .param("lastPullRequestDate", link.lastPullRequestDate())
            .update();
    }

    @Override
    public void updateLastPullRequestDate(Long linkId, OffsetDateTime lastPullRequestDate) {
        jdbcClient.sql("UPDATE github_link SET last_pull_request_date = :lastPullRequestDate WHERE link_id = :linkId")
            .param("linkId", linkId)
            .param("lastPullRequestDate", lastPullRequestDate)
            .update();
    }
}
