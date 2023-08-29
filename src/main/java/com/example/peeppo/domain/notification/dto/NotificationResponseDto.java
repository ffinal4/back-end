package com.example.peeppo.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private Boolean checked;

    public Boolean NotificationResponseDto(Boolean checked) {
        return this.checked = checked;
    }
}
