package edu.java.retry;

import edu.java.configuration.ApplicationConfig;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public class RetryUtils {

    public static ExchangeFilterFunction getRetryFilter(String clientName, ApplicationConfig.RetryConfig retryConfig) {
        Retry retry = getRetry(clientName, retryConfig);
        return (request, next) -> next.exchange(request)
            .flatMap(clientResponse -> Mono.just(clientResponse)
                .filter(response -> clientResponse.statusCode().isError())
                .flatMap(response -> clientResponse.createException())
                .flatMap(Mono::error)
                .thenReturn(clientResponse))
            .retryWhen(retry);
    }

    private static Retry getRetry(String clientName, ApplicationConfig.RetryConfig retryConfig) {
        ApplicationConfig.RetryConfig.RetryItem retryItem = retryConfig.retryItems().stream()
            .filter(item -> item.clientName().equals(clientName)).findFirst()
            .orElseThrow(() -> new RuntimeException("No such client in config"));
        return switch (retryItem.type()) {
            case CONSTANT -> RetryBackoffSpec.fixedDelay(retryItem.maxAttempts(), retryItem.minBackoff())
                .maxBackoff(retryItem.maxBackoff())
                .filter(getErrorFilterForCodes(retryItem.errorCodes()));
            case LINEAR -> LinearRetry.linearRetry(
                retryItem.maxAttempts(),
                retryItem.minBackoff(),
                retryItem.maxBackoff(),
                retryItem.increment()
            ).filter(getErrorFilterForCodes(retryItem.errorCodes()));
            case EXPONENTIAL -> RetryBackoffSpec.backoff(retryItem.maxAttempts(), retryItem.minBackoff())
                .maxBackoff(retryItem.maxBackoff())
                .jitter(retryItem.jitter())
                .filter(getErrorFilterForCodes(retryItem.errorCodes()));
        };
    }

    private static Predicate<Throwable> getErrorFilterForCodes(List<Integer> errorCodes) {
        return throwable -> {
            if (throwable instanceof WebClientResponseException error) {
                return errorCodes.contains(error.getStatusCode().value());
            } else {
                return true;
            }
        };
    }
}
