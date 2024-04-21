package utils.retry.type;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.reactivestreams.Publisher;
import org.springframework.retry.ExhaustedRetryException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

public class LinearRetry extends Retry {
    public final Duration minBackoff;
    public final Duration maxBackoff;
    public final Duration increment;
    public final Long maxAttempts;
    public final Predicate<Throwable> errorFilter;
    public final Supplier<Scheduler> backoffSchedulerSupplier;

    public LinearRetry(
        Duration minBackoff, Duration maxBackoff, Duration increment, long maxAttempts,
        Predicate<Throwable> errorFilter,
        Supplier<Scheduler> backoffSchedulerSupplier
    ) {
        this.minBackoff = minBackoff;
        this.maxBackoff = maxBackoff;
        this.increment = increment;
        this.maxAttempts = maxAttempts;
        this.errorFilter = errorFilter;
        this.backoffSchedulerSupplier = backoffSchedulerSupplier;
    }

    public static LinearRetry linearRetry(
        Long maxAttempts,
        Duration minBackoff,
        Duration maxBackoff,
        Duration increment
    ) {
        return new LinearRetry(
            minBackoff,
            maxBackoff,
            increment,
            maxAttempts,
            error -> true,
            Schedulers::single
        );
    }

    public LinearRetry filter(Predicate<Throwable> errorFilter) {
        return new LinearRetry(
            this.minBackoff,
            this.maxBackoff,
            this.increment,
            this.maxAttempts,
            errorFilter,
            this.backoffSchedulerSupplier
        );
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> retrySignalFlux) {
        return Flux.deferContextual((cv) ->
            retrySignalFlux.contextWrite(cv).concatMap((retryWhenState) -> {
                RetrySignal copy = retryWhenState.copy();
                Throwable currentFailure = copy.failure();
                long iteration = copy.totalRetries();
                if (currentFailure == null) {
                    return Mono.error(new IllegalStateException("Retry.RetrySignal#failure() not expected to be null"));
                } else if (!this.errorFilter.test(currentFailure)) {
                    return Mono.error(currentFailure);
                } else if (iteration >= this.maxAttempts) {
                    return Mono.error(new ExhaustedRetryException("Retries exceeded"));
                } else {
                    Duration nextBackoff;
                    try {
                        nextBackoff = this.minBackoff.plus(increment.multipliedBy(iteration));
                        if (nextBackoff.compareTo(this.maxBackoff) > 0) {
                            nextBackoff = this.maxBackoff;
                        }
                    } catch (ArithmeticException exception) {
                        nextBackoff = this.maxBackoff;
                    }
                    return
                        Mono.delay(
                            nextBackoff,
                            this.backoffSchedulerSupplier.get()
                        );
                }
            }).onErrorStop());
    }
}
