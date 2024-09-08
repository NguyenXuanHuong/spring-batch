package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import com.example.spring_batch.batch.repository.StudentScoreRepository;
import com.example.spring_batch.batch.repository.Top3StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class HandleMissingScoreStudent {
    private static final Logger log = LogManager.getLogger(HandleMissingScoreStudent.class);

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txm;
    private final StudentScoreRepository studentScoreRepository;
    private final Top3StudentRepository top3StudentRepository;

    public HandleMissingScoreStudent(JobRepository jobRepository, PlatformTransactionManager txm
            , StudentScoreRepository studentScoreRepository, Top3StudentRepository top3StudentRepository) {
        this.jobRepository = jobRepository;
        this.txm = txm;
        this.studentScoreRepository = studentScoreRepository;
        this.top3StudentRepository = top3StudentRepository;
    }

    @Bean
    Tasklet handleMissingScoreStudentTasklet() {
        return ((contribution, context) -> {
            log.error("found some missing score student");
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
