package com.example.peeppo.domain.rating.scheduler;

import com.example.peeppo.domain.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RatingScheduler {

    private final RatingService ratingService;
    @Scheduled(cron = "0 0 6 * * SUN") // 매주 일요일 새벽 6시에 실행
    @Transactional
    public void resetPrices()  {
        ratingService.resetPrices();
    }
}
