package com.example.spring_batch.batch.processor;

import com.example.spring_batch.batch.entity.ExtractedStudentInfoEntity;
import com.example.spring_batch.batch.entity.StudentScoreDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentScoreProcessor implements ItemProcessor<StudentScoreDto, ExtractedStudentInfoEntity> {
    @Override
    public ExtractedStudentInfoEntity process(StudentScoreDto studentScoreDto){
        System.out.println("processing");
        ExtractedStudentInfoEntity extractedStudentInfoEntity = new ExtractedStudentInfoEntity();
        extractedStudentInfoEntity.setScore(studentScoreDto.getScore());
        extractedStudentInfoEntity.setStudentName(studentScoreDto.getName());
        return extractedStudentInfoEntity;
    }
}
