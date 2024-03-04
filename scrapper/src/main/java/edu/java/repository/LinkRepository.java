package edu.java.repository;

import edu.java.model.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class LinkRepository {

    public Optional<Link> getLinkByChatId(Long userId) {
        return Optional.empty();
    }

    public Link addLink(Long chatId, URI url) {
        return new Link(1L, url);
    }

    public List<Link> getLinksByChatId(Long chatId) {
        return List.of(new Link(1L, URI.create("https://test.com")));
    }

    public Optional<Link> getLinksByChatIdAndUri(Long chatId, URI url) {
        return Optional.of(new Link(1L, url));
    }

    public Link deleteLinkById(Long id) {
        return new Link(id, URI.create("https://test.com"));
    }
}
