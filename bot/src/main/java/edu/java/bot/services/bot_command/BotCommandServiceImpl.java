package edu.java.bot.services.bot_command;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.RemoveLinkRequest;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.exceptions.api.base.ApiException;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BotCommandServiceImpl implements BotCommandService {
    private final ScrapperClient scrapperClient;

    public BotCommandServiceImpl(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String registerChat(Long tgChatId) {
        try {
            scrapperClient.registerChat(tgChatId);
            return "Вы успешно зарегистрированы!";
        } catch (ApiException ex) {
            return "Ошибка регистрации: " + ex.getDescription();
        }
    }

    @Override
    public String listLinks(Long tgChatId) {
        try {
            List<String> links = scrapperClient.listLinks(tgChatId).getLinks().stream()
                .map(linkResponse -> linkResponse.getUrl().toString()).toList();
            return String.join(";\n", links);
        } catch (ApiException ex) {
            return "Ошибка получения ссылок: " + ex.getDescription();
        }
    }

    @Override
    public String addLink(Long tgChatId, String url) {
        try {
            LinkResponse response = scrapperClient.addLink(tgChatId, new AddLinkRequest(URI.create(url)));
            return "Отслеживаем изменения по ссылке: " + response.getUrl().toString();
        } catch (ApiException ex) {
            return "Ошибка добавления ссылки: " + ex.getDescription();
        }
    }

    @Override
    public String removeLink(Long tgChatId, String url) {
        try {
            LinkResponse response = scrapperClient.deleteLink(tgChatId, new RemoveLinkRequest(URI.create(url)));
            return "Прекращаем отслеживание изменений по ссылке: " + response.getUrl().toString();
        } catch (ApiException ex) {
            return "Ошибка удаления ссылки: " + ex.getDescription();
        }
    }
}
