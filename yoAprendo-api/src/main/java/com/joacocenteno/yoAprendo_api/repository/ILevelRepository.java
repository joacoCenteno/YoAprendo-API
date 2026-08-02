package com.joacocenteno.yoAprendo_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.Level;

@Repository
public interface ILevelRepository extends JpaRepository<Level, Long>{
    List<Level> findByCourseCourseIdOrderByLevelOrder(Long course_id);
    Boolean existsByCourseCourseIdAndLevelName(Long course_id, String level_name);
}
