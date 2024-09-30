package com.example.spring_batch.batch.processor;

import com.example.spring_batch.batch.dto.StudentNameAndScoreDto;
import com.example.spring_batch.batch.dto.StudentScoreDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ExtractNameAndScore implements ItemProcessor<StudentScoreDto, StudentNameAndScoreDto> {
    @Override
    public StudentNameAndScoreDto process(StudentScoreDto item) throws Exception {
        StudentNameAndScoreDto studentNameAndScoreDto = new StudentNameAndScoreDto();
        studentNameAndScoreDto.setName(item.getName());
        studentNameAndScoreDto.setScore(item.getScore());
        return studentNameAndScoreDto;
    }
}
