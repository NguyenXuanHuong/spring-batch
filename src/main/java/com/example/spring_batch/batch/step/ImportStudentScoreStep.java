package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.dto.StudentExtractedInfoDto;
import com.example.spring_batch.batch.dto.StudentScoreDto;
import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
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
                .<StudentScoreDto, StudentExtractedInfoDto>chunk(2, platformTransactionManager)
                .reader(jsonItemReader(null))
                .processor(studentScoreProcessor)
                .writer(jsonFileItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<StudentScoreDto> jsonItemReader(@Value("#{jobParameters[jsonFilePath]}") String filePath) {
        return new JsonItemReaderBuilder<StudentScoreDto>()
                .name("JsonItemReader")
                .jsonObjectReader(new JacksonJsonObjectReader<>(StudentScoreDto.class))
                .resource(new ClassPathResource(filePath))
                .build();
    }

    @Bean
    public JsonFileItemWriter<StudentExtractedInfoDto> jsonFileItemWriter() {
        WritableResource resource = new FileSystemResource("src/main/resources/json/output.json");
        return new JsonFileItemWriterBuilder<StudentExtractedInfoDto>()
                .name("jsonFileItemWriter")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(resource)
                .build();
    }
}
