package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.dto.StudentScoreDto;
import com.example.spring_batch.batch.entity.StudentScoreEntity;
import com.example.spring_batch.batch.listener.ImportStudentScoreListener;
import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportStudentScoreStep {

    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager platformTransactionManager;
    private final StudentScoreProcessor studentScoreProcessor;
    private final ImportStudentScoreListener importStudentScoreListener;


    public ImportStudentScoreStep(JobRepository jobRepository
            , EntityManagerFactory entityManagerFactory
            , PlatformTransactionManager platformTransactionManager
            , StudentScoreProcessor studentScoreProcessor, ImportStudentScoreListener importStudentScoreListener) {
        this.jobRepository = jobRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.platformTransactionManager = platformTransactionManager;
        this.studentScoreProcessor = studentScoreProcessor;
        this.importStudentScoreListener = importStudentScoreListener;
    }

    @Bean
    public Step studentScoreCSVtoDB() {
        return new StepBuilder("studentScoreCSVtoDB", jobRepository)
                .<StudentScoreDto, StudentScoreEntity>chunk(3, platformTransactionManager)
                .reader(studentScoreFileReader())
                .processor(studentScoreProcessor)
                .writer(studentScoreItemWriter())
                .listener(importStudentScoreListener)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<StudentScoreDto> studentScoreFileReader(){
        return new FlatFileItemReaderBuilder<StudentScoreDto>()
                .resource(new ClassPathResource("csv/student-score.csv"))
                .name("studentScoreFileReader").delimited().delimiter(",")
                .names("name,age,score,gender,schoolName".split(","))
                .linesToSkip(1)
                .fieldSetMapper(new FieldSetMapper<StudentScoreDto>() {
                    @Override
                    public StudentScoreDto mapFieldSet(FieldSet fieldSet) {
                        StudentScoreDto studentScoreDto = new StudentScoreDto();
                        studentScoreDto.setName(fieldSet.readString("name"));
                        studentScoreDto.setAge(fieldSet.readInt("age"));
                        try {
                            studentScoreDto.setScore(fieldSet.readInt("score"));
                        }catch (Exception e){
                            studentScoreDto.setScore(null);
                        }
                        studentScoreDto.setGender(fieldSet.readString("gender"));
                        studentScoreDto.setSchoolName(fieldSet.readString("schoolName"));
                        return studentScoreDto;
                    }
                })
                .build();

    }

    private JpaItemWriter<StudentScoreEntity> studentScoreItemWriter(){
        return new JpaItemWriterBuilder<StudentScoreEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
