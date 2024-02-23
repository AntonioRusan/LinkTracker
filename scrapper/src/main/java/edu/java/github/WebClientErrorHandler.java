package edu.java.github;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class WebClientErrorHandler {
    private WebClientErrorHandler() {
    }

    public static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody)));
            } else if (clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody)));
            } else {
                return Mono.just(clientResponse);
            }
        });
    }
}
