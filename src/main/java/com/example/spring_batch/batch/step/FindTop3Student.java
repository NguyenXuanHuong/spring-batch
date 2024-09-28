package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.entity.StudentScoreEntity;
import com.example.spring_batch.batch.entity.Top3StudentEntity;
import com.example.spring_batch.batch.repository.StudentScoreRepository;
import com.example.spring_batch.batch.repository.Top3StudentRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.scope.context.StepContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
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
    @StepScope
    public Tasklet top3StudentTasklet(@Value("#{jobExecutionContext['myKey']}") String name){

        return ((contribution, context) -> {
            var stepExecution = StepSynchronizationManager.getContext().getStepExecution();
            var jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
            String value = (String) jobExecutionContext.get("myKey");
            System.out.println("value:" + value);
            System.out.println("myKey:" + name);
            List<StudentScoreEntity> top3Students =
                    studentScoreRepository.findTop3OrderByScoreDesc(PageRequest.of(0, 3));
            List<Top3StudentEntity> studentInTop3EntityList = new ArrayList<>();
            for(StudentScoreEntity studentScoreEntity: top3Students){
                Top3StudentEntity top3StudentEntity = new Top3StudentEntity();
                top3StudentEntity.setStudentId(studentScoreEntity.getId());
                top3StudentEntity.setScore(studentScoreEntity.getScore());
                studentInTop3EntityList.add(top3StudentEntity);
            }
            top3StudentRepository.saveAll(studentInTop3EntityList);
            return RepeatStatus.FINISHED;
        }
        );

    }
    @Bean
    Step findTop3StudentStep() {
        return new StepBuilder("findTop3StudentStep", jobRepository)
                .tasklet(top3StudentTasklet(null), txm)
                .build();
    }
}
