package com.example.peeppo.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponseDto {

    private Boolean isRequest;

    private Boolean isAuction;

    public NotificationResponseDto(Boolean isRequest, Boolean isAuction) {
        this.isRequest = isRequest;
        this.isAuction = isAuction;
    }
}
