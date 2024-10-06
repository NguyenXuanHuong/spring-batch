package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.dto.StudentScoreDto;
import com.example.spring_batch.batch.entity.StudentScoreEntity;
import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportStudentScoreStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final StudentScoreProcessor studentScoreProcessor;

    public ImportStudentScoreStep(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager
            , StudentScoreProcessor studentScoreProcessor) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.studentScoreProcessor = studentScoreProcessor;
    }

    @Bean
    public Step studentScoreCSVtoDB() {
        return new StepBuilder("studentScoreCSVtoDB", jobRepository)
                .<StudentScoreDto, StudentScoreEntity>chunk(2, platformTransactionManager)
                .reader(studentScoreFileReader())
                .processor(studentScoreProcessor)
                .writer(new ItemWriter<StudentScoreEntity>() {
                    @Override
                    public void write(Chunk<? extends StudentScoreEntity> chunk) throws Exception {
                        System.out.println("------------complete the chunk -----------");
                    }
                })
//                .taskExecutor(taskExecutor())
                .build();
    }
    private FlatFileItemReader<StudentScoreDto> studentScoreFileReader(){
        return new FlatFileItemReaderBuilder<StudentScoreDto>()
                .resource(new ClassPathResource("csv/student-score.csv"))
                .name("studentScoreFileReader").delimited().delimiter(",")
                .names("name,age,score,gender,schoolName".split(","))
                .linesToSkip(1)
                .targetType(StudentScoreDto.class)
                .build();

    }
    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setThreadNamePrefix("Thread N: ");
        return executor;
    }
}
