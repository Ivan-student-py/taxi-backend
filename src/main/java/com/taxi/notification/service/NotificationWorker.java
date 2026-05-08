package com.taxi.notification.service;

import com.taxi.notification.entity.NotificationStatus;
import com.taxi.notification.entity.NotificationTask;
import com.taxi.notification.metrics.NotificationMetrics;
import com.taxi.notification.repository.NotificationTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationWorker {

    private final NotificationTaskRepository taskRepo;
    private final NotificationMetrics metrics;

    @Async("notificationTaskExecutor")
    public void processNextTask() {
        NotificationTask task = null;

        try {
            task = taskRepo.lockNextPendingTask().orElse(null);

            if (task == null) {
                log.trace("No pending tasks. Worker idle.");
                return;
            }

            log.info("Processing task #{}: recipient={} type={}, message='{}'",
                    task.getId(), task.getRecipientId(), task.getRecipientType(), task.getMessage());

            updateTaskStatus(task.getId(), NotificationStatus.IN_PROGRESS, task.getAttempts());

            boolean sent = simulateSendNotification(task);

            if (sent) {
                updateTaskStatus(task.getId(), NotificationStatus.SENT, task.getAttempts());
                metrics.recordSuccess();
                log.info("Task #{} sent successfully", task.getId());
            } else {
                handleFailure(task);
            }

        } catch (Exception e) {
            log.error("Unexpected error processing task #{}: {}",
                    task != null ? task.getId() : "unknown", e.getMessage());
            if (task != null) {
                handleFailure(task);
            }
        }
    }

    private void handleFailure(NotificationTask task) {
        int newAttempts = task.getAttempts() + 1;
        final int MAX_ATTEMPTS = 3;

        metrics.recordRetry();

        if (newAttempts >= MAX_ATTEMPTS) {
            updateTaskStatus(task.getId(), NotificationStatus.FAILED, newAttempts);
            metrics.recordFailure();
            log.warn("Task #{} failed after {} attempts", task.getId(), newAttempts);
        } else {
            updateTaskStatus(task.getId(), NotificationStatus.PENDING, newAttempts);
            log.info("Task #{} scheduled for retry (attempt {}/{})",
                    task.getId(), newAttempts, MAX_ATTEMPTS);
        }
    }

    @Transactional
    public void updateTaskStatus(Long taskId, NotificationStatus newStatus, int attempts) {
        NotificationTask task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        task.setStatus(newStatus);
        task.setAttempts(attempts);
        taskRepo.save(task);

        log.debug("Task #{} status updated to {} (attempts={})",
                taskId, newStatus, attempts);
    }

    private boolean simulateSendNotification(NotificationTask task) {
        boolean success = Math.random() > 0.3;

        if (success) {
            log.debug("Simulated send to {}: {}", task.getRecipientType(), task.getMessage());
        } else {
            log.warn("Simulated send FAILED for task #{} (will retry)", task.getId());
        }

        return success;
    }
}