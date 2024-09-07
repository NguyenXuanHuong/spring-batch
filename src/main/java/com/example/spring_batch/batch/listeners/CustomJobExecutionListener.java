package com.example.spring_batch.batch.listeners;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;

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
