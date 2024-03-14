package edu.java.services.links_api;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import api.scrapper.models.RemoveLinkRequest;
import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.models.Link;
import edu.java.repositories.ChatRepository;
import edu.java.repositories.LinkRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static edu.java.exceptions.api.ApiError.LINK_ALREADY_ADDED;
import static edu.java.exceptions.api.ApiError.LINK_NOT_FOUND;
import static edu.java.exceptions.api.ApiError.TG_CHAT_NOT_FOUND;

@Service
public class LinksApiServiceImpl implements LinksApiService {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    public LinksApiServiceImpl(LinkRepository linkRepository, ChatRepository chatRepository) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        if (chatRepository.getChatById(tgChatId).isPresent()) {
            return new ResponseEntity<>(
                new ListLinksResponse(
                    linkRepository.getLinksByChatId(tgChatId)
                        .stream()
                        .map(link -> new LinkResponse(link.id(), link.url()))
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
        if (chatRepository.getChatById(tgChatId).isPresent()) {
            List<Link> chatLinks = linkRepository.getLinksByChatId(tgChatId);
            Optional<Link> foundLinkOpt = chatLinks.stream()
                .filter(link -> link.url().equals(addLinkRequest.getLink()))
                .findFirst();
            if (foundLinkOpt.isPresent()) {
                throw new ConflictException(LINK_ALREADY_ADDED);
            } else {
                Link addedLink = linkRepository.addLink(tgChatId, addLinkRequest.getLink());
                return new ResponseEntity<>(
                    new LinkResponse(addedLink.id(), addedLink.url()),
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
        if (chatRepository.getChatById(tgChatId).isPresent()) {
            Optional<Link> foundLinkOpt = linkRepository.getLinksByChatIdAndUri(tgChatId, removeLinkRequest.getLink());
            if (foundLinkOpt.isPresent()) {
                Link deletedLink = linkRepository.deleteLinkById(foundLinkOpt.get().id());
                return new ResponseEntity<>(
                    new LinkResponse(deletedLink.id(), deletedLink.url()),
                    HttpStatus.OK
                );
            } else {
                throw new NotFoundException(LINK_NOT_FOUND);
            }
        } else {
            throw new NotFoundException(TG_CHAT_NOT_FOUND);
        }
    }
}
