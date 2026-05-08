package com.taxi.notification.scheduler;

import com.taxi.notification.service.NotificationWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final NotificationWorker worker;

    @Scheduled(fixedDelay = 2000)
    public void pollAndProcess() {
        log.trace("Scheduler polling for tasks...");
        worker.processNextTask();
    }
}