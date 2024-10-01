package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.dto.StudentExtractedInfoDto;
import com.example.spring_batch.batch.dto.StudentScoreDto;
import com.example.spring_batch.batch.processor.StudentScoreProcessor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
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
                .<StudentScoreDto, StudentExtractedInfoDto>chunk(1, platformTransactionManager)
                .reader(itemReader(null))
                .processor(studentScoreProcessor)
                .writer(personXmlFileWriter())
                .build();
    }

    @StepScope
    @Bean
    public StaxEventItemReader<StudentScoreDto> itemReader(
            @Value("#{jobParameters[csvFilePath]}") String filePath) {
        return new StaxEventItemReaderBuilder<StudentScoreDto>()
                .name("studentXmlFileReader")
                .resource(new ClassPathResource(filePath))
                .addFragmentRootElements("studentScoreDto")
                .unmarshaller(jaxb2Marshaller())
                .build();
    }
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(StudentScoreDto.class);
        return jaxb2Marshaller;
    }

    @Bean
    public Jaxb2Marshaller jaxb2MarshallerWriter() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(StudentExtractedInfoDto.class);
        return jaxb2Marshaller;
    }

    @Bean
    public StaxEventItemWriter<StudentExtractedInfoDto> personXmlFileWriter() {
        WritableResource resource = new FileSystemResource("src/main/resources/csv/output.xml");
        return new StaxEventItemWriterBuilder<StudentExtractedInfoDto>()
                .name("studentExtractedInfoItemWriter")
                .resource(resource)
                .marshaller(jaxb2MarshallerWriter())
                .rootTagName("extractedInfo")
                .build();
    }

}
