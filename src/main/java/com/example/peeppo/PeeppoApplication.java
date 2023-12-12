package com.example.peeppo;

import com.example.peeppo.global.batch.rating.config.BatchConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
@Import({BatchConfig.class})
public class PeeppoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeeppoApplication.class, args);
    }
}
