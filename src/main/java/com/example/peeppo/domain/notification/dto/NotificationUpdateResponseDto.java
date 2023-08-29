package com.example.peeppo.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationUpdateResponseDto {

    private Boolean isUpdate;
    private Long count;

    public NotificationUpdateResponseDto(Boolean isUpdate, Long count) {
        this.isUpdate = isUpdate;
        this.count = count;
    }
}
