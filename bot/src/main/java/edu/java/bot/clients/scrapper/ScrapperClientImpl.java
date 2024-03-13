package edu.java.bot.clients.scrapper;

import edu.java.bot.api.exceptions.base.BadRequestException;
import edu.java.bot.api.exceptions.base.ConflictException;
import edu.java.bot.api.exceptions.base.NotFoundException;
import edu.java.bot.clients.scrapper.model.AddLinkRequest;
import edu.java.bot.clients.scrapper.model.LinkResponse;
import edu.java.bot.clients.scrapper.model.ListLinksResponse;
import edu.java.bot.clients.scrapper.model.RemoveLinkRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})
@Service
public class ScrapperClientImpl implements ScrapperClient {
    private final WebClient webClient;
    private final static Logger LOGGER = LogManager.getLogger();

    @Autowired
    public ScrapperClientImpl(@Qualifier("scrapperWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void registerChat(Long id) {
        webClient
            .post()
            .uri("/api/tg-chat/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                response -> switch (response.statusCode().value()) {
                    case 400 -> response.bodyToMono(BadRequestException.class).flatMap(Mono::error);
                    case 409 -> response.bodyToMono(ConflictException.class).flatMap(Mono::error);
                    default -> Mono.error(new Exception("Неизвестная ошибка"));
                }
            )
            .bodyToMono(Void.class)
            .block();
    }

    @Override
    public void deleteChat(Long id) {
        webClient
            .delete()
            .uri("/api/tg-chat/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                response -> switch (response.statusCode().value()) {
                    case 400 -> response.bodyToMono(BadRequestException.class).flatMap(Mono::error);
                    case 404 -> response.bodyToMono(NotFoundException.class).flatMap(Mono::error);
                    default -> Mono.error(new Exception("Неизвестная ошибка"));
                }
            )
            .bodyToMono(Void.class)
            .block();
    }

    @Override
    public ListLinksResponse listLinks(Long tgChatId) {
        return webClient
            .get()
            .uri("/api/links")
            .header("id", String.valueOf(tgChatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                response -> switch (response.statusCode().value()) {
                    case 400 -> response.bodyToMono(BadRequestException.class).flatMap(Mono::error);
                    case 404 -> response.bodyToMono(NotFoundException.class).flatMap(Mono::error);
                    default -> Mono.error(new Exception("Неизвестная ошибка"));
                }
            )
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    @Override
    public LinkResponse addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri("/api/links")
            .header("id", String.valueOf(tgChatId))
            .bodyValue(addLinkRequest)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                response -> switch (response.statusCode().value()) {
                    case 400 -> response.bodyToMono(BadRequestException.class).flatMap(Mono::error);
                    case 404 -> response.bodyToMono(NotFoundException.class).flatMap(Mono::error);
                    case 409 -> response.bodyToMono(ConflictException.class).flatMap(Mono::error);
                    default -> Mono.error(new Exception("Неизвестная ошибка"));
                }
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

    @Override
    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri("/api/links")
            .header("id", String.valueOf(tgChatId))
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(removeLinkRequest)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                response -> switch (response.statusCode().value()) {
                    case 400 -> response.bodyToMono(BadRequestException.class).flatMap(Mono::error);
                    case 404 -> response.bodyToMono(NotFoundException.class).flatMap(Mono::error);
                    default -> Mono.error(new Exception("Неизвестная ошибка"));
                }
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
