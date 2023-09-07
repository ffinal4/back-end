//package com.example.peeppo.domain.notification.service;
//
//import com.example.peeppo.domain.notification.controller.NotificationController;
//import com.example.peeppo.domain.notification.dto.NotificationResponseDto;
//import com.example.peeppo.domain.notification.dto.NotificationUpdateResponseDto;
//import com.example.peeppo.domain.notification.entity.Notification;
//import com.example.peeppo.domain.notification.repository.EmitterRepository;
//import com.example.peeppo.domain.notification.repository.NotificationRepository;
//import com.example.peeppo.domain.user.entity.User;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//    private final EmitterRepository emitterRepository;
//
//    // 메시지 알림
//    public SseEmitter subscribe(Long userId) {
//        // 현재 클라이언트를 위한 sseEmitter 생성
//        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
//        try {
//            // 연결
//            sseEmitter.send(SseEmitter.event().name("connect"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // user 의 pk 값을 key 값으로 해서 sseEmitter 를 저장
//        NotificationController.sseEmitters.put(userId, sseEmitter);
//
//        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));
//        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));
//        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));
//
//        return sseEmitter;
//    }
//
//    @Transactional
//    public void send(User receiver, Review review, String content) {
//        Notification notification = createNotification(receiver, review, content);
//        String id = String.valueOf(receiver.getId());
//        notificationRepository.save(notification);
//        Map<String, SseEmitter> sseEmitters = emitterRepositoryImpl.findAllStartWithByUserId(id);
//        sseEmitters.forEach(
//                (key, emitter) -> {
//                    emitterRepository.saveEventCache(key, notification);
//                    sendToClient(emitter, key, NotificationResponse.from(notification));
//                }
//        );
//    }
//
//    private void sendToClient(SseEmitter emitter, String id, Object data) {
//        try {
//            emitter.send(SseEmitter.event()
//                    .id(id)
//                    .name("sse")
//                    .data(data));
//        } catch (IOException exception) {
//            emitterRepository.deleteById(id);
//            throw new RuntimeException("연결 오류!");
//        }
//    }
//
//    private Notification createNotification(User user, Review review, String content) {
//        return Notification.builder()
//                .user(user)
//                .content(content)
//                .review(review)
//                .url("/reviews/" + review.getId())
//                .isRead(false)
//                .build();
//    }
//
//    @Transactional
//    public NotificationsResponse findAllById(LoginMember loginMember) {
//        List<NotificationResponse> responses = notificationRepository.findAllByReceiverId(loginMember.getId()).stream()
//                .map(NotificationResponse::from)
//                .collect(Collectors.toList());
//        long unreadCount = responses.stream()
//                .filter(notification -> !notification.isRead())
//                .count();
//
//        return NotificationsResponse.of(responses, unreadCount);
//    }
//
//    @Transactional
//    public void readNotification(Long id) {
//        Notification notification = notificationRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알림입니다."));
//        notification.read();
//    }
//
////    public NotificationResponseDto getNotification(User user) {
////        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
////  //      List<NotificationResponseDto> notificationResponseDtos = new ArrayList<>();
////
////  //      for(Notification notification : notificationList){
////            NotificationResponseDto notificationResponseDto = new NotificationResponseDto(notification.getChecked());
////  //          notificationResponseDtos.add(notificationResponseDto);
////  //      }
////        return notificationResponseDto;
////    }
////
/////*    public ResponseDto deleteNotification(Long notificationId) {
////
////        notificationRepository.deleteById(notificationId);
////
////        return new ResponseDto("알림 확인 완료", HttpStatus.OK.value(), "OK");
////    }*/
////
////    public NotificationUpdateResponseDto getNotificationAuction(User user) {
////        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
////
////        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsAuction(), notification.getAuctionCount());
////
////        notification.setIsAuction(true);
////        notification.auctionRead(0L);
////
//// /*       if(notification.getIsRequest() == true){
////            notification.Checked(true);
////        }*/
////
////        notificationRepository.save(notification);
////
////        return notificationUpdateResponseDto;
////    }
////
////    public NotificationUpdateResponseDto getNotificationRequest(User user) {
////        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
////
////        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsRequest(), notification.getRequestCount());
////
////        notification.setIsRequest(true);
////        notification.requestRead(0L);
////
/////*        if(notification.getIsAuction() == true){
////            notification.Checked(true);
////        }*/
////
////        notificationRepository.save(notification);
////
////        return notificationUpdateResponseDto;
////    }
////
////    public NotificationUpdateResponseDto getNotificationMessage(User user) {
////        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
////
////        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsRequest(), notification.getRequestCount());
////
////        notification.setIsRequest(true);
////        notification.requestRead(0L);
////
//// /*       if(notification.getIsAuction() == true){
////            notification.Checked(true);
////        }*/
////
////        notificationRepository.save(notification);
////
////        return notificationUpdateResponseDto;
////    }
//}
