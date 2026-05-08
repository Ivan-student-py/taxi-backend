package com.taxi.notification.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class NotificationMetrics {

    private Counter successCounter;
    private Counter failureCounter;
    private Counter retryCounter;

    public NotificationMetrics(MeterRegistry registry) {
        if (registry != null) {
            this.successCounter = Counter.builder("notifications.sent.total")
                    .description("Total number of successfully sent notifications")
                    .tag("service", "taxi-backend")
                    .register(registry);

            this.failureCounter = Counter.builder("notifications.failed.total")
                    .description("Total number of failed notification attempts")
                    .tag("service", "taxi-backend")
                    .register(registry);

            this.retryCounter = Counter.builder("notifications.retries.total")
                    .description("Total number of retry attempts")
                    .tag("service", "taxi-backend")
                    .register(registry);
        }
    }

    public void recordSuccess() {
        if (successCounter != null) successCounter.increment();
    }

    public void recordFailure() {
        if (failureCounter != null) failureCounter.increment();
    }

    public void recordRetry() {
        if (retryCounter != null) retryCounter.increment();
    }
}