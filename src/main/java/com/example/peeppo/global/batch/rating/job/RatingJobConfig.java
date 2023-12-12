package com.example.peeppo.global.batch.rating.job;

import com.example.peeppo.global.batch.rating.step.RatingStepConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RatingJobConfig {

    public static final String JOB_NAME = "EXAMPLE_JOB";
    private final Step ratingStep;

    public RatingJobConfig(
            @Qualifier(RatingStepConfig.STEP_NAME) Step ratingStep) {
        this.ratingStep = ratingStep;
    }

    @Bean(JOB_NAME)
    public Job exampleJob(JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(ratingStep)
//                .start(RatingJobConfig)
                .build();
    }
}
