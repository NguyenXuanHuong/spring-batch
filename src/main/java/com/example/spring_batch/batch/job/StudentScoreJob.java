package com.example.spring_batch.batch.job;

import com.example.spring_batch.batch.StepExitStatus;
import com.example.spring_batch.batch.step.FindTop3Student;
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
    private final Step importStudentScoreCSVtoDB;
    private final Step findTop3Student;
    private final Step handleMissingScoreStudentStep;


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
    public Job studentScoreJobConfig() {
        return new JobBuilder("studentScoreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importStudentScoreCSVtoDB)
                .on(StepExitStatus.NULL_SCORE_STUDENT_EXIST.name()).to(handleMissingScoreStudentStep)
                .from(importStudentScoreCSVtoDB).on("*").to(findTop3Student)
                .end()
                .build();
    }

}
