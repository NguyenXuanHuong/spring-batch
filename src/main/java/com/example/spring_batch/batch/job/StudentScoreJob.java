package com.example.spring_batch.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class StudentScoreJob {
    private final JobRepository jobRepository;
    private final Step studentScoreCSVtoDB;


    @Bean
    ApplicationRunner manuallyJobRunner(JobLauncher jobLauncher, Job studentScoreProcessJob) {
        return args -> {
            var jobParameters = new JobParametersBuilder()
                    .addString("job-run-date", String.valueOf(new Date()))
                    .toJobParameters();
            var run = jobLauncher.run(studentScoreProcessJob, jobParameters);
            var instanceId = run.getJobInstance().getInstanceId();
            System.out.println("job instanceId: " + instanceId);
        };
    }

    public StudentScoreJob(
            JobRepository jobRepository
            , @Qualifier("studentScoreCSVtoDB") Step studentScoreCSVtoDB) {
        this.jobRepository = jobRepository;
        this.studentScoreCSVtoDB = studentScoreCSVtoDB;
    }

    @Bean
    public Job studentScoreProcessJob() {
        return new JobBuilder("studentScoreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(studentScoreCSVtoDB)
                .build();

    }

}
