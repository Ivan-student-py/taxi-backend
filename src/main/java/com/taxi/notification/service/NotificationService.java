package com.taxi.notification.service;

import com.taxi.notification.dto.NotificationCreateRequest;
import com.taxi.notification.entity.NotificationStatus;
import com.taxi.notification.entity.NotificationTask;
import com.taxi.notification.repository.NotificationTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationTaskRepository notificationRepo;

    @Transactional
    public NotificationTask createNotification(NotificationCreateRequest req) {
        NotificationTask task = NotificationTask.builder()
                .tripId(req.tripId())
                .recipientType(req.recipientType())
                .recipientId(req.recipientId())
                .message(req.message())
                .status(NotificationStatus.PENDING)
                .attempts(0)
                .build();
        return notificationRepo.save(task);
    }

    public List<NotificationTask> findByTripId(Long tripId) {
        return notificationRepo.findByTripIdOrderByCreatedAtAsc(tripId);
    }

    public List<NotificationTask> findAll() {
        return notificationRepo.findAll();
    }
}