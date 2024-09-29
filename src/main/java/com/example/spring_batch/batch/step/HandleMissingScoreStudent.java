package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import com.example.spring_batch.batch.repository.StudentScoreRepository;
import com.example.spring_batch.batch.repository.Top3StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@Slf4j
public class HandleMissingScoreStudent {
    private static final Logger log = LogManager.getLogger(HandleMissingScoreStudent.class);

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txm;

    public HandleMissingScoreStudent(JobRepository jobRepository, PlatformTransactionManager txm) {
        this.jobRepository = jobRepository;
        this.txm = txm;
    }

    @Bean
    Tasklet handleMissingScoreStudentTasklet() {
        return ((contribution, context) -> {
            if(StepSynchronizationManager.getContext() != null){
                StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
                ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
                String valueFromSecondStep = "valueFromSecondStep";
                jobExecutionContext.put("key-from-another-step", valueFromSecondStep);
            }
            log.warn("found some missing score student");
            return RepeatStatus.FINISHED;
        }
        );

    }
    @Bean
    Step handleMissingScoreStudentStep() {
        return new StepBuilder("handleMissingScoreStudentStep", jobRepository)
                .tasklet(handleMissingScoreStudentTasklet(), txm)
                .build();
    }
}
