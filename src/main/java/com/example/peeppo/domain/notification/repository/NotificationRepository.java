package com.example.peeppo.domain.notification.repository;

import com.example.peeppo.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByUserUserId(Long userId);

    Long countByUserUserIdAndIsAuction(Long userId, Boolean checked);

    Long countByUserUserIdAndIsRequest(Long userId, Boolean checked);
}
