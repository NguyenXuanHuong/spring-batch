package com.example.spring_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class SpringBatchApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
