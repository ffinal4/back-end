package com.example.peeppo.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestingResponseDto {

    private Boolean test;

    public Boolean TestingResponseDto(Boolean test) {
        return this.test = test;
    }
}
