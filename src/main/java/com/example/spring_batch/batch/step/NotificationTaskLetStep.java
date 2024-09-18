package com.example.spring_batch.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class NotificationTaskLetStep {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;

    public NotificationTaskLetStep(PlatformTransactionManager platformTransactionManager, JobRepository jobRepository) {
        this.platformTransactionManager = platformTransactionManager;
        this.jobRepository = jobRepository;
    }

    @Bean
    public Step jobStartedNotificationTaskletStep() {
         return new StepBuilder("jobStartedNotificationTaskletStepName", jobRepository)
                .tasklet(tasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Start to execute job");
            return RepeatStatus.FINISHED;
        };
    }
}
