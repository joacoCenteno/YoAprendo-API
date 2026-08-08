package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.UserStatisticsResponse;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.model.ExerciseProgress;
import com.joacocenteno.yoAprendo_api.model.PorgressStatus;
import com.joacocenteno.yoAprendo_api.repository.IExerciseProgressRepository;
import com.joacocenteno.yoAprendo_api.repository.ILessonRepository;
import com.joacocenteno.yoAprendo_api.repository.IProgressRepository;
import com.joacocenteno.yoAprendo_api.repository.IUserRepository;

@Service
public class StatisticsService implements IStatisticsService{

    @Autowired
    IUserRepository user_repo;

    @Autowired
    IProgressRepository progress_repo;

    @Autowired
    IExerciseProgressRepository exercise_progress_repo;

    @Autowired
    ILessonRepository lessons_repo;

    @Override
    public UserStatisticsResponse getUsersStatics(Long user_id) {
        if(user_id == null) throw new BadRequestException("Por favor, especifique ID del Usuario");

        if(!user_repo.existsById(user_id)) throw new ResourceNotFoundException("Usuario con ID "+user_id+" inexistente");

        Integer total_lessons = progress_repo.findByUserId(user_id).size();
        Integer count_lessons_completed = progress_repo.countByUserIdAndProgressStatus(user_id, PorgressStatus.COMPLETED);
        Integer count_lessons_in_progress = progress_repo.countByUserIdAndProgressStatus(user_id, PorgressStatus.IN_PROGRESS);
        Integer count_lessons_not_started = lessons_repo.findAll().size() - count_lessons_completed - count_lessons_in_progress;

        List<ExerciseProgress> list_exercises = exercise_progress_repo.findByUserId(user_id);
        Integer count_exercises_completed = exercise_progress_repo.countByUserIdAndCompletedTrue(user_id);

        Integer total_attempts = 0;
        
        for (ExerciseProgress exerciseProgress : list_exercises) {
            total_attempts += exerciseProgress.getAttempts();
        }   

        Double progress_percent = list_exercises.size() == 0
        ? 0.0
        : (count_exercises_completed.doubleValue() / list_exercises.size()) * 100.0;

        UserStatisticsResponse user_statics = UserStatisticsResponse.builder()
                                                        .user_id(user_id)
                                                        .lessons_total(total_lessons)
                                                        .lessons_completed(count_lessons_completed)
                                                        .lessons_in_progress(count_lessons_in_progress)
                                                        .lessons_not_started(count_lessons_not_started)
                                                        .exercises_total(list_exercises.size())
                                                        .exercises_completed(count_exercises_completed)
                                                        .total_attempts(total_attempts)
                                                        .progress_percent(progress_percent)
                                                        .build();

        return user_statics;
    }

}
