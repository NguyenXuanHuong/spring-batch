package com.example.spring_batch.batch.job;

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


    public StudentScoreJob(
            JobRepository jobRepository,
            @Qualifier("studentScoreCSVtoDB") Step studentScoreCSVtoDB) {
        this.jobRepository = jobRepository;
        this.studentScoreCSVtoDB = studentScoreCSVtoDB;
    }

    @Bean
    public Job studentScoreJobConfig() {
        return new JobBuilder("studentScoreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(studentScoreCSVtoDB)
                .build();

    }


}
