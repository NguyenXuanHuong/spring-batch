package com.example.spring_batch.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class ImportStudentScoreStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final ItemProcessor<String, Integer> firstProcessor;
    private final ItemProcessor<Integer, String> secondProcessor;
    private final ItemProcessor<String, Integer> thirdProcessor;

    public ImportStudentScoreStep(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager
            , @Qualifier("firstProcessor") ItemProcessor<String, Integer> firstProcessor
            , @Qualifier("secondProcessor")ItemProcessor<Integer, String> secondProcessor
            , @Qualifier("thirdProcessor")ItemProcessor<String, Integer> thirdProcessor
    ) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.firstProcessor = firstProcessor;
        this.secondProcessor = secondProcessor;
        this.thirdProcessor = thirdProcessor;
    }

    @Bean
    public Step studentScoreCSVtoDB() {
        return new StepBuilder("studentScoreCSVtoDB", jobRepository)
                .<String, Integer>chunk(1, platformTransactionManager)
                .reader(new ItemReader<>() {
                    private boolean methodIsCalled = false;
                    @Override
                    public String read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
                        if (!methodIsCalled) {
                            methodIsCalled = true;
                            return "output String from item reader";
                        }
                        return null;
                    }
                })
                .processor(compositeProcessor())
                .writer(chunk -> System.out.println("item passed to ItemProcessor: " + chunk.getItems().get(0)))
                .build();
    }

    @Bean
    public CompositeItemProcessor<String, Integer> compositeProcessor() {
        CompositeItemProcessor<String, Integer> processor = new CompositeItemProcessor<>();
        processor.setDelegates(List.of(firstProcessor, secondProcessor, thirdProcessor));
        return processor;
    }

}
