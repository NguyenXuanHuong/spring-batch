package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.repository.StudentScoreRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

public class FindTop3Student {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txm;
    private final StudentScoreRepository studentScoreRepository;
    private final Top3StudentRepository top3StudentRepository;

    public FindTop3Student(JobRepository jobRepository, PlatformTransactionManager txm, StudentScoreRepository studentScoreRepository, Top3StudentRepository top3StudentRepository) {
        this.jobRepository = jobRepository;
        this.txm = txm;
        this.studentScoreRepository = studentScoreRepository;
        this.top3StudentRepository = top3StudentRepository;
    }

    @Bean
    Tasklet top3StudentTasklet() {
        return ((contribution, context) -> {
            List<StudentScoreEntity> top3HighestScoreStudents =
                    studentScoreRepository.findTop3OrderByScoreDesc(PageRequest.of(0, 3));
            List<StudentInTop3Entity> studentInTop3EntityList = new ArrayList<>();
            for(StudentScoreEntity studentScoreEntity: top3HighestScoreStudents){
                StudentInTop3Entity studentInTop3Entity = new StudentInTop3Entity();
                studentInTop3Entity.setStudentId(studentScoreEntity.getId());
                studentInTop3Entity.setScore(studentScoreEntity.getScore());
                studentInTop3EntityList.add(studentInTop3Entity);
            }
            top3StudentRepository.saveAll(studentInTop3EntityList);
            return RepeatStatus.FINISHED;
        }
        );
    }
    @Bean
    Step extractTop3StudentStep() {
        return new StepBuilder("extractTop3StudentStep", jobRepository)
                .tasklet(top3StudentTasklet(), txm)
                .build();
    }
}
