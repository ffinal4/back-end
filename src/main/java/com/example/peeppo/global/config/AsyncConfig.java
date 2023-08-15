package com.example.peeppo.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "DefaultAsync")
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5); // 기본 thread 수
        taskExecutor.setMaxPoolSize(30); // 최대 thread 수
        taskExecutor.setQueueCapacity(10); // thread pool 에서 사용할 최대 queue 크기
        taskExecutor.setThreadNamePrefix("Async-"); // thread prefix
        taskExecutor.initialize();

        return taskExecutor;
    }

}
