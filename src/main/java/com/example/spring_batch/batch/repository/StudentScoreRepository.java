package com.example.spring_batch.batch.repository;

import com.example.spring_batch.batch.entity.StudentScoreEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentScoreRepository extends JpaRepository<StudentScoreEntity, Integer> {
    @Query("SELECT s FROM StudentScoreEntity s ORDER BY s.score DESC")
    List<StudentScoreEntity> findTop3OrderByScoreDesc(Pageable pageable);

    boolean existsByScoreNull();
}
