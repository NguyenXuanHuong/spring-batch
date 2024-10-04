package com.example.spring_batch.batch.processor;

import com.example.spring_batch.batch.entity.ExtractedStudentInfoEntity;
import com.example.spring_batch.batch.entity.StudentScoreEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentScoreProcessor implements ItemProcessor<StudentScoreEntity, ExtractedStudentInfoEntity> {
    @Override
    public ExtractedStudentInfoEntity process(StudentScoreEntity studentScoreEntity){
        System.out.println("processing");
        ExtractedStudentInfoEntity extractedStudentInfoEntity = new ExtractedStudentInfoEntity();
        extractedStudentInfoEntity.setScore(studentScoreEntity.getScore());
        extractedStudentInfoEntity.setStudentName(studentScoreEntity.getName());
        return extractedStudentInfoEntity;
    }
}
