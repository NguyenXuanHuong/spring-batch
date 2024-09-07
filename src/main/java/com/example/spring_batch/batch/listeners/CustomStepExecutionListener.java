package com.example.spring_batch.batch.listeners;

import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import com.example.spring_batch.batch.repository.StudentScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomStepExecutionListener implements StepExecutionListener {
    private final StudentScoreRepository studentScoreRepository;
    private static final Logger log = LogManager.getLogger(CustomStepExecutionListener.class);

    public CustomStepExecutionListener(StudentScoreRepository studentScoreRepository) {
        this.studentScoreRepository = studentScoreRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("delete all old records");
        studentScoreRepository.deleteAll();
        log.info("remaining item number: " + studentScoreRepository.countByIdNotNull());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (ExitStatus.COMPLETED.equals(stepExecution.getExitStatus())){
            log.info("The step:" + stepExecution.getStepName() + "has finished with status: " + stepExecution.getExitStatus());
            return stepExecution.getExitStatus();
        }
        log.warn("Something bad has happened after step: {}", stepExecution.getStepName());
        return stepExecution.getExitStatus();
    }
}
