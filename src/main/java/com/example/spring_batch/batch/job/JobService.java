package com.example.spring_batch.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobService {
	
	private final Job importStudentScoreJob;
	private final JobLauncher jobLauncher;

    public JobService(Job importStudentScoreJob, JobLauncher jobLauncher) {
        this.importStudentScoreJob = importStudentScoreJob;
        this.jobLauncher = jobLauncher;
    }

    @Async
	public void startJob() {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("currentTime", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameters = new JobParameters(params);
		try {
			JobExecution jobExecution = jobLauncher.run(importStudentScoreJob, jobParameters);
			System.out.println("Job Execution ID = " + jobExecution.getId());
		}catch(Exception e) {
			System.out.println("Exception while starting job");
		}
	}

}
