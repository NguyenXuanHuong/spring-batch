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
    private final Step findTop3Student;
    private final Step handleMissingScoreStudentStep;

    @Bean
    ApplicationRunner JobRunner(JobLauncher jobLauncher, Job studentScoreJobConfigSequence) {
        return args -> {
            var jobParameters = new JobParametersBuilder()
                    .addString("uuid", UUID.randomUUID().toString())
                    .addString("csvFilePath", "csv/student-score.csv")
                    .toJobParameters();
            jobLauncher.run(studentScoreJobConfigSequence, jobParameters);
        };
    }

    public StudentScoreJob(
            JobRepository jobRepository
            , @Qualifier("studentScoreCSVtoDB") Step importStudentScoreCSVtoDB
            , @Qualifier("findTop3StudentStep") Step findTop3Student
            , @Qualifier("handleMissingScoreStudentStep") Step handleMissingScoreStudentStep) {
        this.jobRepository = jobRepository;
        this.importStudentScoreCSVtoDB = importStudentScoreCSVtoDB;
        this.findTop3Student = findTop3Student;
        this.handleMissingScoreStudentStep = handleMissingScoreStudentStep;
    }
    @Bean
    public Job studentScoreJobConfigSequence() {
        return new JobBuilder("studentScoreJob", jobRepository)
                .start(importStudentScoreCSVtoDB)
                .next(handleMissingScoreStudentStep)
                .next(findTop3Student)
                .build();
    }

}
