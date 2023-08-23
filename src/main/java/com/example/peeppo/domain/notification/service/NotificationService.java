package com.example.peeppo.domain.notification.service;

import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.notification.dto.Test;
import com.example.peeppo.domain.notification.dto.TestingResponseDto;
import com.example.peeppo.domain.notification.entity.Notification;
import com.example.peeppo.domain.notification.repository.NotificationRepository;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public TestingResponseDto getNotification(User user) {
        Notification notification = notificationRepository.findByUserUserId(user.getUserId());

        if(notification.getIsAuction() && notification.getIsRequest()){
            return new TestingResponseDto(true);
        }

        notification.read();

        return new TestingResponseDto(false);
    }

//    public ResponseDto deleteNotification(Long notificationId) {
//
//        notificationRepository.deleteById(notificationId);
//
//        return new ResponseDto("알림 확인 완료", HttpStatus.OK.value(), "OK");
//    }

    public Test getNotificationAuction(User user) {
        Long count = notificationRepository.countByUserUserIdAndIsAuction(user.getUserId(), false);

        Test test = new Test(count > 0, count);

        return test;
    }

    public Test getNotificationRequest(User user) {
        Long count = notificationRepository.countByUserUserIdAndIsRequest(user.getUserId(), false);

        Test test = new Test(count > 0, count);

        return test;
    }
}
