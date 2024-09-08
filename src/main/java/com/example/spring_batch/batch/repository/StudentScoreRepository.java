package com.example.spring_batch.batch.repository;

import com.example.spring_batch.batch.entity.StudentScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentScoreRepository extends JpaRepository<StudentScoreEntity, Integer> {
}
