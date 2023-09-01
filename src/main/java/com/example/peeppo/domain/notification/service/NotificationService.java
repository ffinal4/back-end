//package com.example.peeppo.domain.notification.service;
//
//import com.example.peeppo.domain.notification.dto.NotificationUpdateResponseDto;
//import com.example.peeppo.domain.notification.dto.NotificationResponseDto;
//import com.example.peeppo.domain.notification.entity.Notification;
//import com.example.peeppo.domain.notification.repository.NotificationRepository;
//import com.example.peeppo.domain.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//
//    public NotificationResponseDto getNotification(User user) {
//        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
//
//        return new NotificationResponseDto(notification.getChecked());
//    }
//
////    public ResponseDto deleteNotification(Long notificationId) {
////
////        notificationRepository.deleteById(notificationId);
////
////        return new ResponseDto("알림 확인 완료", HttpStatus.OK.value(), "OK");
////    }
//
//    public NotificationUpdateResponseDto getNotificationAuction(User user) {
//        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
//
//        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsAuction(), notification.getAuctionCount());
//
//        notification.setIsAuction(true);
//        notification.auctionRead(0L);
//
//        if(notification.getIsRequest() == true){
//            notification.Checked(true);
//        }
//
//        notificationRepository.save(notification);
//
//        return notificationUpdateResponseDto;
//    }
//
//    public NotificationUpdateResponseDto getNotificationRequest(User user) {
//        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
//
//        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsRequest(), notification.getRequestCount());
//
//        notification.setIsRequest(true);
//        notification.requestRead(0L);
//
//        if(notification.getIsAuction() == true){
//            notification.Checked(true);
//        }
//
//        notificationRepository.save(notification);
//
//        return notificationUpdateResponseDto;
//    }
//
//    public NotificationUpdateResponseDto getNotificationMessage(User user) {
//        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
//
//        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsRequest(), notification.getRequestCount());
//
//        notification.setIsRequest(true);
//        notification.requestRead(0L);
//
//        if(notification.getIsAuction() == true){
//            notification.Checked(true);
//        }
//
//        notificationRepository.save(notification);
//
//        return notificationUpdateResponseDto;
//    }
//}
