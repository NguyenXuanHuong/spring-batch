package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.entity.ExtractedStudentInfoEntity;
import com.example.spring_batch.batch.entity.StudentScoreEntity;
import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportStudentScoreStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final StudentScoreProcessor studentScoreProcessor;
    private final EntityManagerFactory entityManagerFactory;


    public ImportStudentScoreStep(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager
            , StudentScoreProcessor studentScoreProcessor, EntityManagerFactory entityManagerFactory) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.studentScoreProcessor = studentScoreProcessor;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Step studentScoreCSVtoDB() {
        return new StepBuilder("studentScoreCSVtoDB", jobRepository)
                .<StudentScoreEntity, ExtractedStudentInfoEntity>chunk(2, platformTransactionManager)
                .reader(jpaPagingItemReader())
                .processor(studentScoreProcessor)
                .writer(jpaWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<StudentScoreEntity> jpaPagingItemReader(
    ) {
        return new JpaPagingItemReaderBuilder<StudentScoreEntity>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select s from StudentScoreEntity s")
                .pageSize(3)
                .build();
    }

//    @Bean
//    public JpaCursorItemReader<StudentScoreEntity> jpaCursorItemReader() {
//        // data is saved in ResultSet.
//        return new JpaCursorItemReaderBuilder<StudentScoreEntity>()
//                .name("jpaCursorItemReader")
//                .entityManagerFactory(entityManagerFactory)
//                .queryString("SELECT s FROM StudentScoreEntity s")
//                .build();
//    }
    @Bean
    public JpaItemWriter<ExtractedStudentInfoEntity> jpaWriter() {
        JpaItemWriter<ExtractedStudentInfoEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
