package com.joacocenteno.yoAprendo_api.service;

import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressRequest;
import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressResponse;

public interface IExerciseProgress {
    public ExerciseProgressResponse attemptExerciseProgress(ExerciseProgressRequest attempt);
}
