package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressRequest;
import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressResponse;

public interface IExerciseProgressService {
    public ExerciseProgressResponse attemptExerciseProgress(ExerciseProgressRequest attempt);

    public ExerciseProgressResponse findExerciseProgressByUserAndExercise(Long user_id, Long exercise_id);

    public List<ExerciseProgressResponse> findAllExerciseProgressByUser(Long user_id);
}
