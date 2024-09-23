package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.customizedIitemWriter.CustomItemWriter;
import com.example.spring_batch.batch.customizedItemProcessor.CustomizedItemProcessor;
import com.example.spring_batch.batch.customizedItemReader.CustomItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportStudentScoreStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final CustomizedItemProcessor customizedItemProcessor;
    private final CustomItemReader customItemReader;
    private final CustomItemWriter customItemWriter;

    public ImportStudentScoreStep(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager
            , CustomizedItemProcessor customizedItemProcessor, CustomItemReader customItemReader, CustomItemWriter customItemWriter) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.customizedItemProcessor = customizedItemProcessor;
        this.customItemReader = customItemReader;
        this.customItemWriter = customItemWriter;
    }

    @Bean
    public Step studentScoreCSVtoDB() {
        return new StepBuilder("ItemsListProcessingStep", jobRepository)
                .<String, Integer>chunk(4, platformTransactionManager)
                .reader(customItemReader)
                .processor(customizedItemProcessor)
                .writer(customItemWriter)
                .build();
    }
}
