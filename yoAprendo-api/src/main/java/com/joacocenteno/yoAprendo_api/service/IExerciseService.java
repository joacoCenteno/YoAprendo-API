package com.joacocenteno.yoAprendo_api.service;

import com.joacocenteno.yoAprendo_api.dto.ExerciseResponse;
import com.joacocenteno.yoAprendo_api.dto.UpdateExerciseRequest;

public interface IExerciseService {
    public ExerciseResponse editExercise(Long id, UpdateExerciseRequest exercise);
    public ExerciseResponse getExerciseById(Long exercise_id);
}

