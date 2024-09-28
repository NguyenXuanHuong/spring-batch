package com.example.spring_batch.batch.processor;

import com.example.spring_batch.batch.dto.StudentScoreDto;
import com.example.spring_batch.batch.entity.StudentScoreEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentScoreProcessor implements ItemProcessor<StudentScoreDto, StudentScoreEntity> {
    @Override
    public StudentScoreEntity process(StudentScoreDto studentScoreDto){
        if (StepSynchronizationManager.getContext() != null){
            var stepExecutionContext = StepSynchronizationManager.getContext().getStepExecution().getExecutionContext();
            System.out.println("inside item writer get variable value passed from item reader:"
                    + stepExecutionContext.get("step-execution-context-item-reader-key"));
        }
        StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
        try {
            studentScoreEntity.setScore(studentScoreDto.getScore());
        }catch (Exception e){
            studentScoreEntity.setScore(null);

        }
        studentScoreEntity.setAge(studentScoreDto.getAge());
        studentScoreEntity.setName(studentScoreDto.getName());
        studentScoreEntity.setGender(studentScoreDto.getGender());
        studentScoreEntity.setSchoolName(studentScoreDto.getSchoolName());
        return studentScoreEntity;

    }
}
