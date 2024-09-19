package com.example.spring_batch.batch.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.batch.core.Job;

import java.util.Date;

@Service
public class JobService {
    private final JobLauncher jobLauncher;
    private final Job studentScoreProcessJob;

    public JobService(JobLauncher jobLauncher, Job studentScoreProcessJob) {
        this.jobLauncher = jobLauncher;
        this.studentScoreProcessJob = studentScoreProcessJob;
    }

    @Async
    public void startJob() {
        var jobParameters = new JobParametersBuilder()
                .addString("job-run-date", String.valueOf(new Date()))
                .toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(studentScoreProcessJob, jobParameters);
            System.out.println("Job Execution ID = " + jobExecution.getJobInstance().getInstanceId());
        }catch(Exception e) {
            System.out.println("Exception while starting job");
        }
    }
}
