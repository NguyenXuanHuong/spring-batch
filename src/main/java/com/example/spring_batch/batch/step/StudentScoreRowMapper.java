package com.example.spring_batch.batch.step;

import com.example.spring_batch.batch.entity.StudentScoreDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class StudentScoreRowMapper implements RowMapper<StudentScoreDto> {
    public StudentScoreDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        StudentScoreDto studentScoreDto = new StudentScoreDto();
        studentScoreDto.setName(rs.getString("name"));
        studentScoreDto.setAge(rs.getInt("age"));
        studentScoreDto.setScore(rs.getInt("score"));
        studentScoreDto.setGender(rs.getString("gender"));
        studentScoreDto.setSchoolName(rs.getString("school_name"));
        return studentScoreDto;
    }
}
