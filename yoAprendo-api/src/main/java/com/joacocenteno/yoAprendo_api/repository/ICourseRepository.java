package com.joacocenteno.yoAprendo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.Course;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Long>{
    @Query("""
        SELECT COUNT(c) > 0
        FROM Course c
        WHERE c.courseName = :courseName
    """)
    public Boolean existCourseByName(@Param("courseName") String courseName);
}
