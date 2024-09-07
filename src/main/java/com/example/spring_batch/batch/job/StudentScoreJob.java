package com.example.spring_batch.batch.job;

import com.example.spring_batch.batch.listeners.CustomJobExecutionListener;
import com.example.spring_batch.batch.listeners.CustomStepExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentScoreJob {
    private final JobRepository jobRepository;
    private final Step studentScoreCSVtoDB;
    private final CustomJobExecutionListener customJobExecutionListener;

    public StudentScoreJob(
            JobRepository jobRepository
            , @Qualifier("studentScoreCSVtoDB") Step studentScoreCSVtoDB, CustomJobExecutionListener customJobExecutionListener) {
        this.jobRepository = jobRepository;
        this.studentScoreCSVtoDB = studentScoreCSVtoDB;
        this.customJobExecutionListener = customJobExecutionListener;
    }

    @Bean
    public Job studentScoreJobConfig() {
        return new JobBuilder("studentScoreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(studentScoreCSVtoDB)
                .listener(customJobExecutionListener)
                .build();

    }

}
