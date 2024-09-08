package com.example.spring_batch.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleTaskletStepClass {
    private final StepBuilderFactory stepBuilderFactory;

    public SampleTaskletStepClass(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    Tasklet sampleTasklet() {
        return ((contribution, context) -> {
            System.out.println("sample tasklet Step");
            return RepeatStatus.FINISHED;
        }
        );
    }
    @Bean
    Step sampleTaskletStep() {
        return stepBuilderFactory.get("sampleTaskletStep")
                .tasklet(sampleTasklet())
                .build();
    }
}
