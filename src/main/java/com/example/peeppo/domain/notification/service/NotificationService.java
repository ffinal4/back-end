//package com.example.peeppo.domain.notification.service;
//
//import com.example.peeppo.domain.notification.dto.NotificationUpdateResponseDto;
//import com.example.peeppo.domain.notification.dto.NotificationResponseDto;
//import com.example.peeppo.domain.notification.entity.Notification;
//import com.example.peeppo.domain.notification.repository.NotificationRepository;
//import com.example.peeppo.domain.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//
////    public NotificationResponseDto getNotification(User user) {
////        List<Notification> notification = notificationRepository.findByUserUserId(user.getUserId());
////
////        for(Notification notification1 : notification){
////
////        }
////
////        return new NotificationResponseDto(notification.getChecked());
////    }
////
//////    public ResponseDto deleteNotification(Long notificationId) {
//////
//////        notificationRepository.deleteById(notificationId);
//////
//////        return new ResponseDto("알림 확인 완료", HttpStatus.OK.value(), "OK");
//////    }
////
////    public NotificationUpdateResponseDto getNotificationAuction(User user) {
////        List<Notification> notification = notificationRepository.findByUserUserId(user.getUserId());
////
////        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsAuction(), notification.getAuctionCount());
////
////        notification.setIsAuction(true);
////        notification.auctionRead(0L);
////
////        if(notification.getIsRequest() == true){
////            notification.Checked(true);
////        }
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
////        if(notification.getIsAuction() == true){
////            notification.Checked(true);
////        }
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
////        if(notification.getIsAuction() == true){
////            notification.Checked(true);
////        }
////
////        notificationRepository.save(notification);
////
////        return notificationUpdateResponseDto;
////    }
//
//    @Override
//    public SseEmitter subscribe(Long userId, String lastEventId) {
//        String emitterId = makeTimeIncludeId(userId);
//        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));
//        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
//        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
//
//        // 503 에러를 방지하기 위한 더미 이벤트 전송
//        String eventId = makeTimeIncludeId(userId);
//        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");
//
//        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
//        if (hasLostData(lastEventId)) {
//            sendLostData(lastEventId, userId, emitterId, emitter);
//        }
//
//        return emitter;
//    }
//
//    private String makeTimeIncludeId(Long memberId) {
//        return memberId + "_" + System.currentTimeMillis();
//    }
//
//    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
//        try {
//            emitter.send(SseEmitter.event()
//                    .id(eventId)
//                    .data(data));
//        } catch (IOException exception) {
//            emitterRepository.deleteById(emitterId);
//        }
//    }
//
//    private boolean hasLostData(String lastEventId) {
//        return !lastEventId.isEmpty();
//    }
//
//    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
//        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
//        eventCaches.entrySet().stream()
//                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
//    }
//
//    @Override
//    public SseEmitter subscribe(Long memberId, String lastEventId) {
//        String emitterId = makeTimeIncludeId(memberId);
//        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));
//        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
//        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
//
//        // 503 에러를 방지하기 위한 더미 이벤트 전송
//        String eventId = makeTimeIncludeId(memberId);
//        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");
//
//        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
//        if (hasLostData(lastEventId)) {
//            sendLostData(lastEventId, memberId, emitterId, emitter);
//        }
//
//        return emitter;
//    }
//
//    private String makeTimeIncludeId(Long memberId) {
//        return memberId + "_" + System.currentTimeMillis();
//    }
//
//    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
//        try {
//            emitter.send(SseEmitter.event()
//                    .id(eventId)
//                    .data(data));
//        } catch (IOException exception) {
//            emitterRepository.deleteById(emitterId);
//        }
//    }
//
//    private boolean hasLostData(String lastEventId) {
//        return !lastEventId.isEmpty();
//    }
//
//    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
//        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
//        eventCaches.entrySet().stream()
//                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
//    }
//}
