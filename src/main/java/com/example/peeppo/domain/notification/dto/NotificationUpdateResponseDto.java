package com.example.peeppo.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUpdateResponseDto {

    private Boolean newNotification;

    public void NotificationUpdateResponseDto(Boolean newNotification) {
        this.newNotification = newNotification;
    }
}
