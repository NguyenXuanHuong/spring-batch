package com.example.spring_batch.batch.integration;

import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
@Setter
public class FileMessageToJobRequest {
    private Job job;

    public void setJob(Job job) {
        this.job = job;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String filename = "input.file.name";

    @Transformer
    public JobLaunchRequest jobLaunchRequest(Message<File> fileMessage){
        var jobParameters = new JobParametersBuilder();
        jobParameters.addString(filename,fileMessage.getPayload().getAbsolutePath());
        jobParameters.addDate("uniqueness", new Date());
        return new JobLaunchRequest(job,jobParameters.toJobParameters());
    }
}
