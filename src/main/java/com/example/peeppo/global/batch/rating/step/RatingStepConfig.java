package com.example.peeppo.global.batch.rating.step;

import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.batch.rating.job.RatingJobConfig;
import com.example.peeppo.global.batch.rating.job.RatingJobParameter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class RatingStepConfig {

    public static final String STEP_NAME = RatingJobConfig.JOB_NAME + ".RATING_STEP";

    private final EntityManagerFactory entityManagerFactory;
    private final RatingJobParameter ratingJobParameter;

    @Bean(STEP_NAME)
    @JobScope
    public Step ratingeStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<User, User>chunk(ratingJobParameter.getChunkSize(), transactionManager)
                .reader(exampleItemReader())
                .writer(exampleItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<User> exampleItemReader() {
        return new JpaPagingItemReaderBuilder<User>()
                .name("exampleItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(ratingJobParameter.getChunkSize())
                .queryString("SELECT u FROM User u")
                .build();
    }

    @Bean
    public ItemWriter<User> exampleItemWriter() {
        return users -> {
            for (User user : users) {
                System.out.println(user);
            }
        };
    }
}