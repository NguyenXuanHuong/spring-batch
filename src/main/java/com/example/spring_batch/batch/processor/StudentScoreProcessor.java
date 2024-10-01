package com.example.spring_batch.batch.processor;

import com.example.spring_batch.batch.dto.StudentExtractedInfoDto;
import com.example.spring_batch.batch.dto.StudentScoreDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentScoreProcessor implements ItemProcessor<StudentScoreDto, StudentExtractedInfoDto> {
    @Override
    public StudentExtractedInfoDto process(StudentScoreDto studentScoreDto){
        StudentExtractedInfoDto studentExtractedInfoDto = new StudentExtractedInfoDto();
        studentExtractedInfoDto.setScore(studentScoreDto.getScore());
        studentExtractedInfoDto.setName(studentScoreDto.getName());
        return studentExtractedInfoDto;
    }
}
