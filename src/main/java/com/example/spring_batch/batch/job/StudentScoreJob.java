package com.example.spring_batch.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentScoreJob {

    private final Step studentScoreCSVtoDBDeprecated;
    private final JobBuilderFactory jobBuilderFactory;

    public StudentScoreJob(
            Step studentScoreCSVtoDBDeprecated
            , JobBuilderFactory jobBuilderFactory) {
        this.studentScoreCSVtoDBDeprecated = studentScoreCSVtoDBDeprecated;
        this.jobBuilderFactory = jobBuilderFactory;
    }


    //( spring batch 4 - spring boot 2) Deprecated
    @Bean
    public Job importStudentScoreJob(){
        return jobBuilderFactory.get("importStudentScore")
                .incrementer(new RunIdIncrementer())
                .start(studentScoreCSVtoDBDeprecated)
                .build();
    }

}
