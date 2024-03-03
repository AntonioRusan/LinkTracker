package edu.java.bot.clients.scrapper;

import edu.java.bot.clients.scrapper.model.AddLinkRequest;
import edu.java.bot.clients.scrapper.model.LinkResponse;
import edu.java.bot.clients.scrapper.model.ListLinksResponse;
import edu.java.bot.clients.scrapper.model.RemoveLinkRequest;

public interface ScrapperClient {
    void registerChat(Long id);

    void deleteChat(Long id);

    ListLinksResponse listLinks(Long tgChatId);

    LinkResponse addLink(Long tgChatId, AddLinkRequest addLinkRequest);

    LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest);
}
