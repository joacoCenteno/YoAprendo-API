package com.joacocenteno.yoAprendo_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.Exercise;

@Repository
public interface IExerciseRepository extends JpaRepository<Exercise, Long>{
    List<Exercise> findByLessonLessonId(Long id_lesson);

    Long countByLessonLessonId(Long id_lesson);
}
