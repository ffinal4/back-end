package com.example.peeppo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
//@EnableAspectJAutoProxy
public class PeeppoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeeppoApplication.class, args);
    }
}
