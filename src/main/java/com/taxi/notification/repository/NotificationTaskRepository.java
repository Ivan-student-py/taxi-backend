package com.taxi.notification.repository;

import com.taxi.notification.entity.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    @Query(value = """
        SELECT * FROM notification_tasks 
        WHERE status = 'PENDING' 
        ORDER BY created_at ASC 
        LIMIT 1 FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    Optional<NotificationTask> lockNextPendingTask();

    List<NotificationTask> findByTripIdOrderByCreatedAtAsc(Long tripId);
}