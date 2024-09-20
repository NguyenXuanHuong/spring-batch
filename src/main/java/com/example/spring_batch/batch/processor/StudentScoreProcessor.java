package com.example.spring_batch.batch.processor;

import com.example.spring_batch.batch.dto.StudentScoreDto;
import com.example.spring_batch.batch.entity.StudentScoreEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@StepScope
public class StudentScoreProcessor implements ItemProcessor<StudentScoreDto, StudentScoreEntity> {
    private static final Logger log = LogManager.getLogger(StudentScoreProcessor.class);

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public StudentScoreEntity process(StudentScoreDto studentScoreDto){

        String parameterValue = stepExecution.getJobParameters().getString("first-parameter-key");
        System.out.println("Job parameters: " + parameterValue);

        StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
        studentScoreEntity.setScore(studentScoreDto.getScore());
        studentScoreEntity.setAge(studentScoreDto.getAge());
        studentScoreEntity.setName(studentScoreDto.getName());
        studentScoreEntity.setGender(studentScoreDto.getGender());
        studentScoreEntity.setSchoolName(studentScoreDto.getSchoolName());
        log.info("processing item: " + studentScoreDto);
        return studentScoreEntity;
    }
}
