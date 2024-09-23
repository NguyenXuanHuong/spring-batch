package com.example.spring_batch.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentScoreJob {
    private final JobRepository jobRepository;
    private final Step studentScoreCSVtoDB;

    public StudentScoreJob(
            JobRepository jobRepository
            , Step studentScoreCSVtoDB) {
        this.jobRepository = jobRepository;
        this.studentScoreCSVtoDB = studentScoreCSVtoDB;
    }

    @Bean
    public Job studentScoreProcessJob() {
        return new JobBuilder("studentScoreJobName", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(studentScoreCSVtoDB)
                .build();
    }

}
