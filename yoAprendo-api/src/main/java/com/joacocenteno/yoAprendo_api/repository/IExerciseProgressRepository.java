package com.joacocenteno.yoAprendo_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.ExerciseProgress;

@Repository
public interface IExerciseProgressRepository extends JpaRepository<ExerciseProgress, Long>{
    Optional<ExerciseProgress> findByUserIdAndExerciseExerciseId(Long id_user, Long exercise_id);

    List<ExerciseProgress> findByUserId(Long id_user);

    Long countByUserIdAndExerciseLessonLessonIdAndCompletedTrue(Long user_id, Long lesson_id);

    Integer countByUserIdAndCompletedTrue(Long user_id);
}
