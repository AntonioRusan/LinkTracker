package edu.java.repositories.jooq;

import edu.java.models.GitHubLink;
import edu.java.repositories.GitHubLinkRepositoryInterface;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.GithubLink.GITHUB_LINK;

@Repository
public class JooqGitHubLinkRepository implements GitHubLinkRepositoryInterface {

    private final DSLContext dsl;

    public JooqGitHubLinkRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<GitHubLink> findByLinkId(Long linkId) {
        return dsl.selectFrom(GITHUB_LINK)
            .where(GITHUB_LINK.LINK_ID.equal(linkId))
            .fetchOptionalInto(GitHubLink.class);
    }

    @Override
    public Integer add(GitHubLink link) {
        return dsl.insertInto(GITHUB_LINK)
            .set(GITHUB_LINK.LINK_ID, link.linkId())
            .set(GITHUB_LINK.LAST_PULL_REQUEST_DATE, link.lastPullRequestDate())
            .execute();
    }

    @Override
    public void updateLastPullRequestTime(Long linkId, OffsetDateTime lastPullRequestDate) {
        dsl.update(GITHUB_LINK)
            .set(GITHUB_LINK.LAST_PULL_REQUEST_DATE, lastPullRequestDate)
            .where(GITHUB_LINK.LINK_ID.equal(linkId))
            .execute();
    }
}
