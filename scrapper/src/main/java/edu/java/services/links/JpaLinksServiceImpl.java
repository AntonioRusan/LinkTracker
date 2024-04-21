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
import edu.java.models.jpa.ChatEntity;
import edu.java.models.jpa.GitHubLinkEntity;
import edu.java.models.jpa.LinkEntity;
import edu.java.models.jpa.StackOverflowLinkEntity;
import edu.java.repositories.jpa.JpaChatRepository;
import edu.java.repositories.jpa.JpaGitHubLinkRepository;
import edu.java.repositories.jpa.JpaLinkRepository;
import edu.java.repositories.jpa.JpaStackOverflowLinkRepository;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static edu.java.exceptions.api.ApiError.LINK_ALREADY_ADDED;
import static edu.java.exceptions.api.ApiError.LINK_NOT_FOUND;
import static edu.java.exceptions.api.ApiError.TG_CHAT_NOT_FOUND;

public class JpaLinksServiceImpl implements LinksService {

    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    private final JpaGitHubLinkRepository gitHubLinkRepository;
    private final JpaStackOverflowLinkRepository stackOverflowLinkRepository;

    public JpaLinksServiceImpl(
        JpaLinkRepository linkRepository,
        JpaChatRepository chatRepository,
        JpaGitHubLinkRepository gitHubLinkRepository,
        JpaStackOverflowLinkRepository stackOverflowLinkRepository
    ) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
        this.gitHubLinkRepository = gitHubLinkRepository;
        this.stackOverflowLinkRepository = stackOverflowLinkRepository;
    }

    @Override
    public ListLinksResponse getAllLinks(Long tgChatId) {
        Optional<ChatEntity> chatOpt = chatRepository.findById(tgChatId);
        if (chatOpt.isPresent()) {
            ChatEntity chat = chatOpt.get();
            return new ListLinksResponse(
                chat.getLinks()
                    .stream()
                    .map(link -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
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
        Optional<ChatEntity> chatOpt = chatRepository.findById(tgChatId);
        if (chatOpt.isPresent()) {
            ChatEntity chat = chatOpt.get();
            Set<LinkEntity> chatLinks = chat.getLinks();
            Optional<LinkEntity> foundLinkOpt = chatLinks.stream()
                .filter(link -> link.getUrl().equals(addLinkRequest.getLink().toString()))
                .findFirst();
            if (foundLinkOpt.isPresent()) {
                throw new ConflictException(LINK_ALREADY_ADDED);
            } else {
                String url = addLinkRequest.getLink().toString();
                Optional<LinkEntity> foundLinkByUrl = linkRepository.findByUrl(url);
                LinkEntity addedLink;

                if (foundLinkByUrl.isPresent()) {
                    addedLink = foundLinkByUrl.get();
                } else {
                    addedLink =
                        new LinkEntity(addLinkRequest.getLink().toString(), OffsetDateTime.now(), OffsetDateTime.now());
                    addedLink = linkRepository.save(addedLink);
                    switch (URI.create(url).getHost()) {
                        case "github.com" ->
                            gitHubLinkRepository.save(new GitHubLinkEntity(addedLink, OffsetDateTime.now()));
                        case "stackoverflow.com" -> stackOverflowLinkRepository.save(new StackOverflowLinkEntity(
                            addedLink,
                            OffsetDateTime.now()
                        ));
                        default -> {
                        }
                    }
                }
                chat.addLink(addedLink);
                addedLink.addChat(chat);
                return new LinkResponse(addedLink.getId(), addLinkRequest.getLink());
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
        Optional<ChatEntity> chatOpt = chatRepository.findById(tgChatId);
        if (chatOpt.isPresent()) {
            ChatEntity chat = chatOpt.get();
            Set<LinkEntity> chatLinks = chat.getLinks();
            Optional<LinkEntity> foundLinkOpt = chatLinks.stream()
                .filter(link -> link.getUrl().equals(removeLinkRequest.getLink().toString()))
                .findFirst();

            if (foundLinkOpt.isPresent()) {
                LinkEntity link = foundLinkOpt.get();
                chat.deleteLink(link);
                //linkRepository.delete(link.id());
                return
                    new LinkResponse(link.getId(), URI.create(link.getUrl()));
            } else {
                throw new NotFoundException(LINK_NOT_FOUND);
            }
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    public List<Link> findOlderThanIntervalLinks(Duration interval) {
        return linkRepository.findOlderThanIntervalLinks(OffsetDateTime.now().minus(interval)).stream()
            .map(LinkEntity::toLink).toList();
    }

    @Override
    public void updateLinkCheckAndUpdatedTime(Long linkId, OffsetDateTime lastCheckTime, OffsetDateTime updatedAt) {
        Optional<LinkEntity> linkOpt = linkRepository.findById(linkId);
        if (linkOpt.isPresent()) {
            LinkEntity link = linkOpt.get();
            link.setLastCheckTime(lastCheckTime);
            link.setUpdatedAt(updatedAt);
        }

    }

    @Override
    public void updateLinkCheckTime(Long linkId, OffsetDateTime lastCheckTime) {
        Optional<LinkEntity> linkOpt = linkRepository.findById(linkId);
        if (linkOpt.isPresent()) {
            LinkEntity link = linkOpt.get();
            link.setLastCheckTime(lastCheckTime);
        }
    }

    @Override
    public List<Chat> findAllChatByLinkId(Long linkId) {
        Optional<LinkEntity> linkOpt = linkRepository.findById(linkId);
        if (linkOpt.isPresent()) {
            LinkEntity link = linkOpt.get();
            return link.getChats().stream().map(ChatEntity::toChat).toList();
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<GitHubLink> findGitHubByLinkId(Long linkId) {
        return gitHubLinkRepository.findById(linkId).map(GitHubLinkEntity::toGitHubLink);
    }

    @Override
    @Transactional
    public void updateGitHubLinkLastPullRequestDate(Long gitHubLinkId, OffsetDateTime pullRequestDate) {
        Optional<GitHubLinkEntity> linkOpt = gitHubLinkRepository.findById(gitHubLinkId);
        if (linkOpt.isPresent()) {
            GitHubLinkEntity link = linkOpt.get();
            link.setLastPullRequestDate(pullRequestDate);
        }
    }

    @Override
    public Optional<StackOverflowLink> findStackOverflowByLinkId(Long linkId) {
        return stackOverflowLinkRepository.findById(linkId).map(StackOverflowLinkEntity::toStackOverflowLink);
    }

    @Override
    @Transactional
    public void updateStackOverflowLastAnswerDate(Long stackOverflowLinkId, OffsetDateTime answersDate) {
        Optional<StackOverflowLinkEntity> linkOpt = stackOverflowLinkRepository.findById(stackOverflowLinkId);
        if (linkOpt.isPresent()) {
            StackOverflowLinkEntity link = linkOpt.get();
            link.setLastAnswerDate(answersDate);
        }
    }
}
