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
import java.util.ArrayList;
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

    default List<Link> findOlderThanIntervalLinks(Duration interval) {
        return new ArrayList<>();
    }

    default void updateLinkCheckAndUpdatedTime(Long linkId, OffsetDateTime lastCheckTime, OffsetDateTime updatedAt) {
    }

    default void updateLinkCheckTime(Long linkId, OffsetDateTime lastCheckTime) {
    }

    default List<Chat> findAllChatByLinkId(Long linkId) {
        return new ArrayList<>();
    }

    default Optional<GitHubLink> findGitHubByLinkId(Long linkId) {
        return Optional.empty();
    }

    default void updateGitHubLinkLastPullRequestDate(Long gitHubLinkId, OffsetDateTime pullRequestDate) {
    }

    default Optional<StackOverflowLink> findStackOverflowByLinkId(Long linkId) {
        return Optional.empty();
    }

    default void updateStackOverflowLastAnswerDate(Long gitHubLinkId, OffsetDateTime answerDate) {
    }
}
