package com.joacocenteno.yoAprendo_api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressRequest;
import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressResponse;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Exercise;
import com.joacocenteno.yoAprendo_api.model.ExerciseProgress;
import com.joacocenteno.yoAprendo_api.model.User;
import com.joacocenteno.yoAprendo_api.repository.IExerciseProgressRepository;
import com.joacocenteno.yoAprendo_api.repository.IExerciseRepository;
import com.joacocenteno.yoAprendo_api.repository.IUserRepository;


@Service
public class ExerciseProgressService implements IExerciseProgressService{

    @Autowired
    IExerciseProgressRepository exercise_progress_repo;

    @Autowired
    IUserRepository user_repo;

    @Autowired
    IExerciseRepository exercise_repo;

    @Autowired
    IProgressService progress_serv;

    @Override
    public ExerciseProgressResponse attemptExerciseProgress(ExerciseProgressRequest attempt) {
        User user_attempt = user_repo.findById(attempt.getUser_id()).orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "+attempt.getUser_id()+" inexistente"));
        Exercise exercise_attempt = exercise_repo.findById(attempt.getExercise_id()).orElseThrow(() -> new ResourceNotFoundException("Ejercicio con ID "+attempt.getExercise_id()+" inexistente"));

        ExerciseProgress exercise_progress_obtained = exercise_progress_repo.findByUserIdAndExerciseExerciseId(attempt.getUser_id(), attempt.getExercise_id()).orElse(null);

        if(exercise_progress_obtained != null) return existAttemptExerciseProgress(exercise_progress_obtained, attempt.getIs_correct());
        

        ExerciseProgress new_exercise_progress = ExerciseProgress.builder()
                                                            .user(user_attempt)
                                                            .exercise(exercise_attempt)
                                                            .attempts(1)
                                                            .completed(attempt.getIs_correct() ? true : false)
                                                            .lastAcessDate(LocalDateTime.now())
                                                            .build();

        ExerciseProgress exercise_saved = exercise_progress_repo.save(new_exercise_progress);

        progress_serv.updateProgress(exercise_saved.getUser().getId(), exercise_saved.getExercise().getLesson().getLessonId());

        return Mapper.toDto(exercise_saved);

    }

    private ExerciseProgressResponse existAttemptExerciseProgress(ExerciseProgress exercise_progress, Boolean is_correct){
        exercise_progress.setAttempts(exercise_progress.getAttempts() + 1);
        exercise_progress.setLastAcessDate(LocalDateTime.now());

        if (Boolean.TRUE.equals(is_correct)) {
            exercise_progress.setCompleted(true);
        }

        ExerciseProgress exercise_saved = exercise_progress_repo.save(exercise_progress);

        progress_serv.updateProgress(exercise_saved.getUser().getId(), exercise_saved.getExercise().getLesson().getLessonId());

        return Mapper.toDto(exercise_saved);
    }

}
