package edu.java.api.links;

import edu.java.api.exception.base.ConflictException;
import edu.java.api.exception.base.NotFoundException;
import edu.java.api.model.AddLinkRequest;
import edu.java.api.model.LinkResponse;
import edu.java.api.model.ListLinksResponse;
import edu.java.api.model.RemoveLinkRequest;
import edu.java.model.Link;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static edu.java.api.exception.ApiError.LINK_ALREADY_ADDED;
import static edu.java.api.exception.ApiError.LINK_NOT_FOUND;
import static edu.java.api.exception.ApiError.TG_CHAT_NOT_FOUND;

@Service
public class LinksService implements LinksApiDelegate {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    public LinksService(LinkRepository linkRepository, ChatRepository chatRepository) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        if (chatRepository.getChatById(tgChatId).isPresent()) {
            return new ResponseEntity<>(
                new ListLinksResponse(linkRepository.getLinksByChatId(tgChatId)),
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
                return new ResponseEntity<>(
                    new LinkResponse(linkRepository.addLink(tgChatId, addLinkRequest.getLink())),
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
                return new ResponseEntity<>(
                    new LinkResponse(linkRepository.deleteLinkById(foundLinkOpt.get().id())),
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
