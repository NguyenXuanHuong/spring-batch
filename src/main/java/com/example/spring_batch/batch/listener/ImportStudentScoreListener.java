package com.example.spring_batch.batch.listener;

import com.example.spring_batch.batch.StepExitStatus;
import com.example.spring_batch.batch.repository.StudentScoreRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ImportStudentScoreListener implements StepExecutionListener {
    private final StudentScoreRepository studentScoreRepository;

    public ImportStudentScoreListener(StudentScoreRepository studentScoreRepository) {
        this.studentScoreRepository = studentScoreRepository;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return studentScoreRepository.existsByScoreNull()
                ? new ExitStatus(StepExitStatus.NULL_SCORE_STUDENT_EXIST.name()) : ExitStatus.COMPLETED;
    }
}
