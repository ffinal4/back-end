package com.example.peeppo.domain.notification.repository;

import com.example.peeppo.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserUserId(Long userId);

    Long countByUserUserIdAndIsAuction(Long userId, Boolean checked);

    Long countByUserUserIdAndIsRequest(Long userId, Boolean checked);
}
