package com.example.peeppo.global.utils.time;

import com.example.peeppo.domain.auction.dto.TimeRemaining;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    public static TimeRemaining countDownTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(now, localDateTime);
        long hours = ChronoUnit.HOURS.between(now, localDateTime);
        long minutes = ChronoUnit.MINUTES.between(now, localDateTime);
        long seconds = ChronoUnit.SECONDS.between(now, localDateTime);

        return new TimeRemaining(days, hours % 24, minutes % 60, seconds % 60);
    }

}
