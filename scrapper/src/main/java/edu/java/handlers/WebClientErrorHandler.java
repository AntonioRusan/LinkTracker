package edu.java.handlers;

import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class WebClientErrorHandler {
    private WebClientErrorHandler() {
    }

    public static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            HttpStatusCode status = clientResponse.statusCode();
            if (HttpStatus.NOT_FOUND.equals(status)) {
                return clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new NotFoundException(body)));
            }
            if (HttpStatus.BAD_REQUEST.equals(status)) {
                return clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new BadRequestException(body)));
            }
            if (HttpStatus.CONFLICT.equals(status)) {
                return clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new ConflictException(body)));
            }
            return Mono.just(clientResponse);
        });
    }
}
