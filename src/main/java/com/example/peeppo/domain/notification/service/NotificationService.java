package com.example.peeppo.domain.notification.service;

import com.example.peeppo.domain.notification.dto.NotificationResponseDto;
import com.example.peeppo.domain.notification.entity.Notification;
import com.example.peeppo.domain.notification.enums.NotificationStatus;
import com.example.peeppo.domain.notification.repository.EmitterRepository;
import com.example.peeppo.domain.notification.repository.NotificationRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

    // 메시지 알림
    public SseEmitter subscribe(Long userId, String lastEventId) {
        String id = userId + "_" + System.currentTimeMillis();
        // 현재 클라이언트를 위한 sseEmitter 생성 => 생성자를 통해 만료시간 설정
        SseEmitter sseEmitter = emitterRepository.save(id, new SseEmitter(Long.MAX_VALUE));
        try {
            // 연결
            sseEmitter.send(SseEmitter.event().name("connect").data("connected!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, id, sseEmitter);
        }

        // user 의 pk 값을 key 값으로 해서 sseEmitter 를 저장
        //emitterRepository.sseEmitters.put(id, sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> emitterRepository.sseEmitters.remove(userId));
        sseEmitter.onError((e) -> emitterRepository.sseEmitters.remove(userId));

        return sseEmitter;
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String id, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithUserId(String.valueOf(id));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
    }
    @Transactional
    public void send(User receiver, NotificationStatus notificationStatus, String content) {
        Notification notification = createNotification(receiver, notificationStatus, content);
        String id = String.valueOf(receiver.getUserId());
        String eventId = id + "_" + System.currentTimeMillis();
        notificationRepository.save(notification);
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithByUserId(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponseDto.from(notification));
                }
        );
    }
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteByUserId(id);
            throw new RuntimeException("연결 오류!");
        }
    }

    private Notification createNotification(User user, NotificationStatus notificationStatus, String content) {
        return Notification.builder()
                .receiver(user)
                .content(content)
                .notificationStatus(notificationStatus)
                .isRead(false)
                .build();
    }

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

    @Transactional
    public void readNotification(Long id, User user) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알림입니다."));
        notification.read();
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDto> getNotification(User user) {
        List<Notification> notificationList = notificationRepository.findAllByUserUserIdOrderByCreatedAtDesc(user.getUserId());
        List<NotificationResponseDto> notificationResponseDtos = new ArrayList<>();

        for(Notification notification : notificationList){
            NotificationResponseDto notificationResponseDto = new NotificationResponseDto(notification);
            notificationResponseDtos.add(notificationResponseDto);
            notification.read();
        }
        return notificationResponseDtos;
    }

    @Transactional
    public ResponseDto deleteNotification(Long notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()-> new IllegalArgumentException("해당하는 알림은 존재하지 않습니다"));
        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("접근권한이 없습니다.");
        }
        notificationRepository.deleteById(notificationId);
        return new ResponseDto("알림 확인 완료", HttpStatus.OK.value(), "OK");
    }
//
//    public NotificationUpdateResponseDto getNotificationAuction(User user) {
//        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
//
//        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsAuction(), notification.getAuctionCount());
//
//        notification.setIsAuction(true);
//        notification.auctionRead(0L);
//
// /*       if(notification.getIsRequest() == true){
//            notification.Checked(true);
//        }*/
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
///*        if(notification.getIsAuction() == true){
//            notification.Checked(true);
//        }*/
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
// /*       if(notification.getIsAuction() == true){
//            notification.Checked(true);
//        }*/
//
//        notificationRepository.save(notification);
//
//        return notificationUpdateResponseDto;
//    }
}
