package com.example.peeppo.domain.notification.service;

import com.example.peeppo.domain.notification.controller.NotificationController;
import com.example.peeppo.domain.notification.dto.NotificationResponseDto;
import com.example.peeppo.domain.notification.dto.NotificationUpdateResponseDto;
import com.example.peeppo.domain.notification.entity.Notification;
import com.example.peeppo.domain.notification.repository.NotificationRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 메시지 알림
    public SseEmitter subscribe(Long userId) {
        // 현재 클라이언트를 위한 sseEmitter 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            // 연결
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // user 의 pk 값을 key 값으로 해서 sseEmitter 를 저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));

        return sseEmitter;
    }

    public NotificationResponseDto getNotification(User user) {
        Notification notification = notificationRepository.findByUserUserId(user.getUserId());
  //      List<NotificationResponseDto> notificationResponseDtos = new ArrayList<>();

  //      for(Notification notification : notificationList){
            NotificationResponseDto notificationResponseDto = new NotificationResponseDto(notification.getChecked());
  //          notificationResponseDtos.add(notificationResponseDto);
  //      }

        return notificationResponseDto;
    }

/*    public ResponseDto deleteNotification(Long notificationId) {

        notificationRepository.deleteById(notificationId);

        return new ResponseDto("알림 확인 완료", HttpStatus.OK.value(), "OK");
    }*/

    public NotificationUpdateResponseDto getNotificationAuction(User user) {
        Notification notification = notificationRepository.findByUserUserId(user.getUserId());

        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsAuction(), notification.getAuctionCount());

        notification.setIsAuction(true);
        notification.auctionRead(0L);

 /*       if(notification.getIsRequest() == true){
            notification.Checked(true);
        }*/

        notificationRepository.save(notification);

        return notificationUpdateResponseDto;
    }

    public NotificationUpdateResponseDto getNotificationRequest(User user) {
        Notification notification = notificationRepository.findByUserUserId(user.getUserId());

        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsRequest(), notification.getRequestCount());

        notification.setIsRequest(true);
        notification.requestRead(0L);

/*        if(notification.getIsAuction() == true){
            notification.Checked(true);
        }*/

        notificationRepository.save(notification);

        return notificationUpdateResponseDto;
    }

    public NotificationUpdateResponseDto getNotificationMessage(User user) {
        Notification notification = notificationRepository.findByUserUserId(user.getUserId());

        NotificationUpdateResponseDto notificationUpdateResponseDto = new NotificationUpdateResponseDto(notification.getIsRequest(), notification.getRequestCount());

        notification.setIsRequest(true);
        notification.requestRead(0L);

 /*       if(notification.getIsAuction() == true){
            notification.Checked(true);
        }*/

        notificationRepository.save(notification);

        return notificationUpdateResponseDto;
    }
}
