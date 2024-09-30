package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.dto.StudentNameAndScoreDto;
import com.example.spring_batch.batch.dto.StudentScoreDto;
import com.example.spring_batch.batch.processor.ExtractNameAndScore;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportStudentScoreStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final ExtractNameAndScore extractNameAndScore;


    public ImportStudentScoreStep(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager, ExtractNameAndScore extractNameAndScore) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.extractNameAndScore = extractNameAndScore;
    }

    @Bean
    public Step studentScoreCSVtoDB() {
        return new StepBuilder("studentScoreCSVtoDB", jobRepository)
                .<StudentScoreDto, StudentNameAndScoreDto>chunk(3, platformTransactionManager)
                .reader(studentScoreFileReader(null))
                .processor(extractNameAndScore)
                .writer(writer())
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<StudentScoreDto> studentScoreFileReader(
            @Value("#{jobParameters[csvFilePath]}") String filePath){
        System.out.println("reading file at: " + filePath);
        return new FlatFileItemReaderBuilder<StudentScoreDto>()
                .resource(new ClassPathResource(filePath))
                .name("studentScoreFileReader").delimited().delimiter(",")
                .names("name,age,score,gender,schoolName".split(","))
                .linesToSkip(1)
//                .targetType(StudentScoreDto.class)
//                 manually mapping the csv item to the dto field.
                .fieldSetMapper(new FieldSetMapper<StudentScoreDto>() {
                    @Override
                    public StudentScoreDto mapFieldSet(FieldSet fieldSet) {
                        StudentScoreDto studentScoreDto = new StudentScoreDto();
//                        studentScoreDto.setName(fieldSet.readString(1));
//                        studentScoreDto.setAge(fieldSet.readInt(2));
//                        studentScoreDto.setScore(fieldSet.readInt(3));
                        studentScoreDto.setName(fieldSet.readString("name"));
                        studentScoreDto.setAge(fieldSet.readInt("age"));
                        studentScoreDto.setScore(fieldSet.readInt("score"));
                        studentScoreDto.setGender(fieldSet.readString("gender"));
                        studentScoreDto.setSchoolName(fieldSet.readString("schoolName"));
                        return studentScoreDto;
                    }
                })

                .build();

    }

    @Bean
    public FlatFileItemWriter<StudentNameAndScoreDto> writer() {
        return new FlatFileItemWriterBuilder<StudentNameAndScoreDto>()
                .name("personItemWriter")
                .resource(new FileSystemResource("src/main/resources/outputData.csv"))
                .delimited()
                .delimiter(",")
                .names("name,score".split(","))
                .build();
    }

}
