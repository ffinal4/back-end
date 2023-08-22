package com.example.peeppo.domain.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRemaining {
    private long days;
    private long hours;
    private long minutes;
    private long seconds;
}
