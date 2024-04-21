package utils.retry.model;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;

public record RetryConfig(
    List<RetryItem> retryItems
) {
    public record RetryItem(
        @NotNull String clientName,
        @NotNull RetryType type,
        Long maxAttempts,
        Duration minBackoff,
        Duration maxBackoff,
        Double jitter,
        Duration increment,
        List<Integer> errorCodes
    ) {
    }
}
