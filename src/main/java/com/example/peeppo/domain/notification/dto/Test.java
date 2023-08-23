package com.example.peeppo.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Test {

    private Boolean isTest;
    private Long count;

    public Test(Boolean isTest, Long count) {
        this.isTest = isTest;
        this.count = count;
    }
}
