package edu.java.services.links;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import api.scrapper.models.RemoveLinkRequest;
import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.models.Chat;
import edu.java.models.GitHubLink;
import edu.java.models.Link;
import edu.java.models.StackOverflowLink;
import edu.java.repositories.jdbc.JdbcChatLinkRepository;
import edu.java.repositories.jdbc.JdbcChatRepository;
import edu.java.repositories.jdbc.JdbcGitHubLinkRepository;
import edu.java.repositories.jdbc.JdbcLinkRepository;
import edu.java.repositories.jdbc.JdbcStackOverflowLinkRepository;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static edu.java.exceptions.api.ApiError.LINK_ALREADY_ADDED;
import static edu.java.exceptions.api.ApiError.LINK_NOT_FOUND;
import static edu.java.exceptions.api.ApiError.TG_CHAT_NOT_FOUND;

@RequiredArgsConstructor
public class JdbcLinksServiceImpl implements LinksService {
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;
    private final JdbcGitHubLinkRepository gitHubLinkRepository;
    private final JdbcStackOverflowLinkRepository stackOverflowLinkRepository;

    @Override
    @Transactional
    public ListLinksResponse getAllLinks(Long tgChatId) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            return new ListLinksResponse(
                chatLinkRepository.findAllLinksByChatId(tgChatId)
                    .stream()
                    .map(link -> new LinkResponse(link.id(), URI.create(link.url())))
                    .toList()
            );
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public LinkResponse addLink(
        Long tgChatId,
        AddLinkRequest addLinkRequest
    ) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            List<Link> chatLinks = chatLinkRepository.findAllLinksByChatId(tgChatId);
            Optional<Link> foundLinkOpt = chatLinks.stream()
                .filter(link -> link.url().equals(addLinkRequest.getLink().toString()))
                .findFirst();
            if (foundLinkOpt.isPresent()) {
                throw new ConflictException(LINK_ALREADY_ADDED);
            } else {
                String url = addLinkRequest.getLink().toString();
                Optional<Link> foundLinkByUrl = linkRepository.findByUrl(url);
                Link addedLink = Link.create(addLinkRequest.getLink().toString());
                Long addedLinkId;
                if (foundLinkByUrl.isPresent()) {
                    addedLinkId = foundLinkByUrl.get().id();
                } else {
                    addedLinkId = linkRepository.add(addedLink);
                    switch (URI.create(url).getHost()) {
                        case "github.com" ->
                            gitHubLinkRepository.add(new GitHubLink(addedLinkId, OffsetDateTime.now()));
                        case "stackoverflow.com" ->
                            stackOverflowLinkRepository.add(new StackOverflowLink(addedLinkId, OffsetDateTime.now()));
                        default -> {
                        }
                    }
                }
                chatLinkRepository.add(tgChatId, addedLinkId);
                return new LinkResponse(addedLinkId, addLinkRequest.getLink());
            }
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public LinkResponse removeLink(
        Long tgChatId,
        RemoveLinkRequest removeLinkRequest
    ) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            List<Link> chatLinks = chatLinkRepository.findAllLinksByChatId(tgChatId);
            Optional<Link> foundLinkOpt = chatLinks.stream()
                .filter(link -> link.url().equals(removeLinkRequest.getLink().toString()))
                .findFirst();

            if (foundLinkOpt.isPresent()) {
                Link link = foundLinkOpt.get();
                chatLinkRepository.delete(tgChatId, link.id());
                //linkRepository.delete(link.id());
                return new LinkResponse(link.id(), URI.create(link.url()));
            } else {
                throw new NotFoundException(LINK_NOT_FOUND);
            }
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    public List<Link> findOlderThanIntervalLinks(Duration interval) {
        return linkRepository.findOlderThanIntervalLinks(interval);
    }

    @Override
    public void updateLinkCheckAndUpdatedTime(Long linkId, OffsetDateTime lastCheckTime, OffsetDateTime updatedAt) {
        linkRepository.updateLastCheckAndUpdatedTime(linkId, lastCheckTime, updatedAt);
    }

    @Override
    public void updateLinkCheckTime(Long linkId, OffsetDateTime lastCheckTime) {
        linkRepository.updateLastCheckTime(linkId, lastCheckTime);
    }

    @Override
    public List<Chat> findAllChatByLinkId(Long linkId) {
        return chatLinkRepository.findAllChatsByLinkId(linkId);
    }

    @Override
    public Optional<GitHubLink> findGitHubByLinkId(Long linkId) {
        return gitHubLinkRepository.findByLinkId(linkId);
    }

    @Override
    public void updateGitHubLinkLastPullRequestDate(Long gitHubLinkId, OffsetDateTime pullRequestDate) {
        gitHubLinkRepository.updateLastPullRequestDate(gitHubLinkId, pullRequestDate);
    }

    @Override
    public Optional<StackOverflowLink> findStackOverflowByLinkId(Long linkId) {
        return stackOverflowLinkRepository.findByLinkId(linkId);
    }

    @Override
    public void updateStackOverflowLastAnswerDate(Long stackOverflowLinkId, OffsetDateTime answersDate) {
        stackOverflowLinkRepository.updateLastAnswerDate(stackOverflowLinkId, answersDate);
    }

}
