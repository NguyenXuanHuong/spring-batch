package com.example.spring_batch.batch.controller;

import com.example.spring_batch.batch.job.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job")
public class JobController {
	
	private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("")
	public String startJob() {
		jobService.startJob();
		return "Job Started...";
	}
}
