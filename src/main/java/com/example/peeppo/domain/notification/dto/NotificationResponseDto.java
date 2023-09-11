package com.example.peeppo.domain.notification.dto;

import com.example.peeppo.domain.notification.entity.Notification;
import com.example.peeppo.domain.notification.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private Boolean checked;
    private String content;
    private String url;
    private NotificationStatus notificationStatus;

    public NotificationResponseDto(Notification notification) {
        this.content = notification.getContent();
        this.url = notification.getUrl();
        this.notificationStatus = notification.getNotificationStatus();
    }


    public static Object from(Notification notification) {
        return new NotificationResponseDto(notification);
    }

    public Boolean NotificationResponseDto(Boolean checked) {
        return this.checked = checked;
    }
}
