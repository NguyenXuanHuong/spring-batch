package com.example.spring_batch.batch.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.batch.core.Job;


@Service
public class JobService {
    private final JobLauncher jobLauncher;
    private final Job studentScoreProcessJob;

    public JobService(JobLauncher jobLauncher, Job studentScoreProcessJob) {
        this.jobLauncher = jobLauncher;
        this.studentScoreProcessJob = studentScoreProcessJob;
    }

    @Async
    public void startJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        var jobParameters = new JobParametersBuilder()
                .addString("first-parameter-key", "firstValue")
                .toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(studentScoreProcessJob, jobParameters);
            System.out.println("Job Execution ID = " + jobExecution.getJobInstance().getInstanceId());
        }catch(Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
