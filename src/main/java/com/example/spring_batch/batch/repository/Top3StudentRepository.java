package com.example.spring_batch.batch.repository;

import com.example.spring_batch.batch.entity.Top3StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Top3StudentRepository extends JpaRepository<Top3StudentEntity, Integer> {
}
