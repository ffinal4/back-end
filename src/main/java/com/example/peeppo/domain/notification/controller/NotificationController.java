package com.example.peeppo.domain.notification.controller;

import com.example.peeppo.domain.notification.dto.NotificationUpdateResponseDto;
import com.example.peeppo.domain.notification.dto.NotificationResponseDto;
import com.example.peeppo.domain.notification.service.NotificationService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;

    //알림 목록
    @GetMapping("/notifications")
    public ApiResponse<NotificationResponseDto> getNotification(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseUtils.ok(notificationService.getNotification(userDetails.getUser()));
    }

//    @DeleteMapping("/notifications/{notificationId}")
//    public ApiResponse<ResponseDto> deleteNotification(@PathVariable Long notificationId) {
//
//        return ResponseUtils.ok(notificationService.deleteNotification(notificationId));
//    }

    @GetMapping("/notifications/auction")
    public ApiResponse<NotificationUpdateResponseDto> getNotificationAuction(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseUtils.ok(notificationService.getNotificationAuction(userDetails.getUser()));
    }

    @GetMapping("/notifications/request")
    public ApiResponse<NotificationUpdateResponseDto> getNotificationRequest(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseUtils.ok(notificationService.getNotificationRequest(userDetails.getUser()));
    }

    @GetMapping("/notifications/message")
    public ApiResponse<NotificationUpdateResponseDto> getNotificationMessage(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseUtils.ok(notificationService.getNotificationMessage(userDetails.getUser()));
    }
}