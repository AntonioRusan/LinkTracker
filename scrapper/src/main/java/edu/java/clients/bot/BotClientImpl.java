package edu.java.clients.bot;

import api.bot.models.LinkUpdate;
import edu.java.exceptions.api.base.BadRequestException;
import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@SuppressWarnings("MagicNumber")
public class BotClientImpl implements BotClient {
    private final WebClient webClient;
    private final static Logger LOGGER = LogManager.getLogger();

    @Autowired
    public BotClientImpl(@Qualifier("botWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void sendUpdate(LinkUpdate request) {
        webClient
            .post()
            .uri("/api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
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
            .bodyToMono(Void.class)
            .block();
    }
}
