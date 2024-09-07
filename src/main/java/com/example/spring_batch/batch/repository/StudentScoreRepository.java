package com.example.spring_batch.batch.repository;

import com.example.spring_batch.batch.entity.StudentScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentScoreRepository extends JpaRepository<StudentScoreEntity, Integer> {

    long countByIdNotNull();
}
