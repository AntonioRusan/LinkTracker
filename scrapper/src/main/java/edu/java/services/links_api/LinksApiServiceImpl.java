package edu.java.services.links_api;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import api.scrapper.models.RemoveLinkRequest;
import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.models.Link;
import edu.java.repositories.JdbcChatLinkRepository;
import edu.java.repositories.JdbcChatRepository;
import edu.java.repositories.JdbcLinkRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static edu.java.exceptions.api.ApiError.LINK_ALREADY_ADDED;
import static edu.java.exceptions.api.ApiError.TG_CHAT_NOT_FOUND;

@Service
public class LinksApiServiceImpl implements LinksApiService {
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    public LinksApiServiceImpl(
        JdbcLinkRepository linkRepository, JdbcChatRepository chatRepository,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
        this.chatLinkRepository = chatLinkRepository;
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            return new ResponseEntity<>(
                new ListLinksResponse(
                    chatLinkRepository.findAllLinksByChatId(tgChatId)
                        .stream()
                        .map(link -> new LinkResponse(link.id(), URI.create(link.url())))
                        .toList()
                ),
                HttpStatus.OK
            );
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(
        Long tgChatId,
        AddLinkRequest addLinkRequest
    ) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            List<Link> chatLinks = chatLinkRepository.findAllLinksByChatId(tgChatId);
            Optional<Link> foundLinkOpt = chatLinks.stream()
                .filter(link -> link.url().equals(addLinkRequest.getLink()))
                .findFirst();
            if (foundLinkOpt.isPresent()) {
                throw new ConflictException(LINK_ALREADY_ADDED);
            } else {
                Link addedLink = Link.create(addLinkRequest.getLink().toString());
                linkRepository.add(addedLink);
                chatLinkRepository.add(tgChatId, addedLink.id());
                return new ResponseEntity<>(
                    new LinkResponse(addedLink.id(), URI.create(addedLink.url())),
                    HttpStatus.OK
                );
            }
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<LinkResponse> linksDelete(
        Long tgChatId,
        RemoveLinkRequest removeLinkRequest
    ) {
        return new ResponseEntity<>(
            new LinkResponse(1L, URI.create("https://test.com")),
            HttpStatus.OK
        );
        /*if (chatRepository.findById(tgChatId).isPresent()) {
            Optional<Link> foundLinkOpt = linkRepository.getLinksByChatIdAndUri(tgChatId, removeLinkRequest.getLink());
            if (foundLinkOpt.isPresent()) {
                linkRepository.delete(foundLinkOpt.get().id());
                return new ResponseEntity<>(
                    new LinkResponse(deletedLink.id(), deletedLink.url()),
                    HttpStatus.OK
                );
            } else {
                throw new NotFoundException(LINK_NOT_FOUND);
            }
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }*/
    }
}
