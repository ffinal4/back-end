package com.example.peeppo.global.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheScheduler {

    private final CacheManager cacheManager;
    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void updateCacheEverySecond() {
        Cache cache = cacheManager.getCache("allGoodsCache");

        if (cache != null) {
            String updatedValue = "newUpdatedValue"; // 업데이트할 값
            cache.put("defaultKey", updatedValue);
        }
    }

}
