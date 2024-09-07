package com.example.spring_batch.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StudentScoreJob {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final Step studentScoreCSVtoDB;

    /*
    //( spring batch 4 - spring boot 2) Deprecated
    private final JobBuilderFactory jobBuilderFactory;
    */

    public StudentScoreJob(
            JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager
            , @Qualifier("studentScoreCSVtoDB") Step studentScoreCSVtoDB) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.studentScoreCSVtoDB = studentScoreCSVtoDB;
    }

    @Bean
    public Job studentScoreJobConfig() {
        return new JobBuilder("studentScoreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(studentScoreCSVtoDB)
                .build();

    }

    /*
    //( spring batch 4 - spring boot 2) Deprecated
    @Bean
    public Job importStudentScoreJobDeprecated(){
        return jobBuilderFactory.get("importStudentScore")
                .incrementer(new RunIdIncrementer())
                .start(studentScoreCSVtoDBDeprecated())
                .build();
    }
     */

}
