package edu.java.bot.clients.scrapper;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import api.scrapper.models.RemoveLinkRequest;

public interface ScrapperClient {
    void registerChat(Long id);

    void deleteChat(Long id);

    ListLinksResponse listLinks(Long tgChatId);

    LinkResponse addLink(Long tgChatId, AddLinkRequest addLinkRequest);

    LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest);
}
