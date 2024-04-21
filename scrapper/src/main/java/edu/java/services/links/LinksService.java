package edu.java.services.links;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import api.scrapper.models.RemoveLinkRequest;
import edu.java.controllers.links.LinksApiController;
import edu.java.models.Chat;
import edu.java.models.GitHubLink;
import edu.java.models.Link;
import edu.java.models.StackOverflowLink;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A delegate to be called by the {@link LinksApiController}}.
 */

@SuppressWarnings("MultipleStringLiterals")
public interface LinksService {

    default LinkResponse removeLink(
        Long tgChatId,
        RemoveLinkRequest removeLinkRequest
    ) {
        return null;
    }

    default ListLinksResponse getAllLinks(Long tgChatId) {
        return null;
    }

    default LinkResponse addLink(
        Long tgChatId,
        AddLinkRequest addLinkRequest
    ) {
        return null;
    }

    List<Link> findOlderThanIntervalLinks(Duration interval);

    void updateLinkCheckAndUpdatedTime(Long linkId, OffsetDateTime lastCheckTime, OffsetDateTime updatedAt);

    void updateLinkCheckTime(Long linkId, OffsetDateTime lastCheckTime);

    List<Chat> findAllChatByLinkId(Long linkId);

    Optional<GitHubLink> findGitHubByLinkId(Long linkId);

    void updateGitHubLinkLastPullRequestDate(Long gitHubLinkId, OffsetDateTime pullRequestDate);

    Optional<StackOverflowLink> findStackOverflowByLinkId(Long linkId);

    void updateStackOverflowLastAnswerDate(Long gitHubLinkId, OffsetDateTime answerDate);
}
