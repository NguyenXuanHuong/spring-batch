package com.example.spring_batch.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class StudentScoreJob {
    private final JobRepository jobRepository;
    private final Step importStudentScoreCSVtoDB;

    @Bean
    ApplicationRunner JobRunner(JobLauncher jobLauncher, Job studentScoreJobConfigSequence) {
        return args -> {
            var jobParameters = new JobParametersBuilder()
                    .addString("uuid", UUID.randomUUID().toString())
                    .addString("csvFilePath", "csv/student-score.xml")
                    .toJobParameters();
            jobLauncher.run(studentScoreJobConfigSequence, jobParameters);
        };
    }

    public StudentScoreJob(
            JobRepository jobRepository
            , Step importStudentScoreCSVtoDB) {
        this.jobRepository = jobRepository;
        this.importStudentScoreCSVtoDB = importStudentScoreCSVtoDB;
    }
    @Bean
    public Job studentScoreJobConfigSequence() {
        return new JobBuilder("studentScoreJob", jobRepository)
                .start(importStudentScoreCSVtoDB)
                .build();
    }

}
