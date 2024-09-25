package com.example.spring_batch.batch.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomJobExecutionListener implements JobExecutionListener {
    private static final Logger log = LogManager.getLogger(CustomStepExecutionListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("---------------> Before job execution: " + jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("-----------------> After job: " + jobExecution.getJobId());
    }
}
