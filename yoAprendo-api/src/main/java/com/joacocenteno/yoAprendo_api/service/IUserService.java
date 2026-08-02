package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressResponse;
import com.joacocenteno.yoAprendo_api.dto.ProgressResponse;
import com.joacocenteno.yoAprendo_api.dto.UserRequest;
import com.joacocenteno.yoAprendo_api.dto.UserResponse;

public interface IUserService {
    public UserResponse getUserById(Long user_id);
    public UserResponse createUser(UserRequest user);
    public UserResponse editUser(Long user_id, UserRequest user);
    public void deactivateUser(Long user_id);
    public List<ProgressResponse> getAllProgressByUser(Long user_id);
    public ProgressResponse getProgressByUserAndLesson(Long user_id, Long lesson_id);
    public List<ExerciseProgressResponse> getAllExerciseProgressByUser(Long user_id);
    public ExerciseProgressResponse getExerciseProgressByUserAndExercise(Long user_id, Long exercise_id);
}
