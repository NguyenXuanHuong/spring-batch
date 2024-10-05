package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.entity.ExtractedStudentInfoEntity;
import com.example.spring_batch.batch.entity.StudentScoreDto;
import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ImportStudentScoreStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final StudentScoreProcessor studentScoreProcessor;
    private final EntityManagerFactory entityManagerFactory;
    private final StudentScoreRowMapper studentScoreRowMapper;
    private final DataSource dataSource;

    public ImportStudentScoreStep(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager
            , StudentScoreProcessor studentScoreProcessor
            , EntityManagerFactory entityManagerFactory
            , StudentScoreRowMapper studentScoreRowMapper
            , DataSource dataSource) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.studentScoreProcessor = studentScoreProcessor;
        this.entityManagerFactory = entityManagerFactory;
        this.studentScoreRowMapper = studentScoreRowMapper;
        this.dataSource = dataSource;
    }

    @Bean
    public Step studentScoreCSVtoDB() {
        return new StepBuilder("studentScoreCSVtoDB", jobRepository)
                .<StudentScoreDto, ExtractedStudentInfoEntity>chunk(2, platformTransactionManager)
                .reader(jdbcPagingItemReader())
                .processor(studentScoreProcessor)
                .writer(jpaWriter())
                .build();
    }

//    @Bean
//    public JdbcCursorItemReader<StudentScoreDto> jdbcCursorItemReader() {
//        return new JdbcCursorItemReaderBuilder<StudentScoreDto>()
//                .dataSource(dataSource)
//                .name("jdbcCursorItemReader")
//                .sql("SELECT * FROM `spring-batch`.student_score_entity")
//                .rowMapper(studentScoreRowMapper)
//                .build();
//    }

    @Bean
    public JdbcPagingItemReader<StudentScoreDto> jdbcPagingItemReader() {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("id", 5);
        return new JdbcPagingItemReaderBuilder<StudentScoreDto>()
                .name("jdbcPagingItemReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider())
                .parameterValues(parameterValues)
                .rowMapper(studentScoreRowMapper)
                .pageSize(2)
                .build();
    }

    @Bean
    public MySqlPagingQueryProvider queryProvider() {
        final Map<String, org.springframework.batch.item.database.Order> sortKeys = new HashMap<>();
        sortKeys.put("id", org.springframework.batch.item.database.Order.ASCENDING);
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("select *");
        provider.setFromClause("from student_score_entity");
        provider.setWhereClause("where id < :id");
        provider.setSortKeys(sortKeys);
        return provider;
    }


    @Bean
    public JpaItemWriter<ExtractedStudentInfoEntity> jpaWriter() {
        JpaItemWriter<ExtractedStudentInfoEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
