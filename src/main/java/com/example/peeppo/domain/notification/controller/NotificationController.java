package com.example.peeppo.domain.notification.controller;

import com.example.peeppo.domain.notification.dto.NotificationUpdateResponseDto;
import com.example.peeppo.domain.notification.dto.NotificationResponseDto;
import com.example.peeppo.domain.notification.service.NotificationService;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;
    //public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    // 메시지 알림
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        Long userId = userDetails.getUser().getUserId();
        SseEmitter sseEmitter = notificationService.subscribe(userId, lastEventId);

        return sseEmitter;
    }

//    @GetMapping("/notifications")
//    public ResponseEntity<NotificationsResponse> notifications(@Login LoginMember loginMember) {
//        return ResponseEntity.ok().body(notificationService.findAllById(loginMember));
//    }
//
    // 알림 읽기
    @PatchMapping("/notifications/{id}")
    public ResponseEntity<Void> readNotification(@PathVariable Long id,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.readNotification(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 알림 목록 => 읽음 처리
    @GetMapping("/notifications")
    public ApiResponse<List<NotificationResponseDto>> getNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.ok(notificationService.getNotification(userDetails.getUser()));
    }

    // 알림 삭제
    @DeleteMapping("/notifications/{notificationId}")
    public ApiResponse<ResponseDto> deleteNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable Long notificationId) {

        return ResponseUtils.ok(notificationService.deleteNotification(notificationId, userDetails.getUser()));
    }
//
//    @GetMapping("/notifications/auction")
//    public ApiResponse<NotificationUpdateResponseDto> getNotificationAuction(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        return ResponseUtils.ok(notificationService.getNotificationAuction(userDetails.getUser()));
//    }
//
//    @GetMapping("/notifications/request")
//    public ApiResponse<NotificationUpdateResponseDto> getNotificationRequest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        return ResponseUtils.ok(notificationService.getNotificationRequest(userDetails.getUser()));
//    }
//
//    @GetMapping("/notifications/message")
//    public ApiResponse<NotificationUpdateResponseDto> getNotificationMessage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        return ResponseUtils.ok(notificationService.getNotificationMessage(userDetails.getUser()));
//    }
}
