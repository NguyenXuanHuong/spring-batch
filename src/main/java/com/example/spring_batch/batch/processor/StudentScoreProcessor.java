package com.example.spring_batch.batch.processor;

import com.example.spring_batch.batch.dto.StudentScoreDto;
import com.example.spring_batch.batch.entity.StudentScoreEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentScoreProcessor implements ItemProcessor<StudentScoreDto, StudentScoreEntity> {
    private static final Logger log = LoggerFactory.getLogger(StudentScoreProcessor.class);

    @Override
    public StudentScoreEntity process(StudentScoreDto studentScoreDto){
        StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
        studentScoreEntity.setScore(studentScoreDto.getScore());
        studentScoreEntity.setAge(studentScoreDto.getAge());
        studentScoreEntity.setName(studentScoreDto.getName());
        studentScoreEntity.setGender(studentScoreDto.getGender());
        studentScoreEntity.setSchoolName(studentScoreDto.getSchoolName());
        log.info("processing item: " + studentScoreDto.getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }
        return studentScoreEntity;

    }
}
