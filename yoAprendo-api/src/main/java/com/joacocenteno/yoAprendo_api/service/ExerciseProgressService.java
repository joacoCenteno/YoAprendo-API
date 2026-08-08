package com.joacocenteno.yoAprendo_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressRequest;
import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressResponse;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Exercise;
import com.joacocenteno.yoAprendo_api.model.ExerciseProgress;
import com.joacocenteno.yoAprendo_api.model.User;
import com.joacocenteno.yoAprendo_api.repository.IExerciseProgressRepository;
import com.joacocenteno.yoAprendo_api.repository.IExerciseRepository;
import com.joacocenteno.yoAprendo_api.repository.IUserRepository;
import com.joacocenteno.yoAprendo_api.service.validation.ExerciseValidator;


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

    @Autowired
    ExerciseValidator exercise_validator;


    @Override
    public ExerciseProgressResponse attemptExerciseProgress(ExerciseProgressRequest attempt) {
        User user_attempt = user_repo.findById(attempt.getUser_id()).orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "+attempt.getUser_id()+" inexistente"));
        Exercise exercise_attempt = exercise_repo.findById(attempt.getExercise_id()).orElseThrow(() -> new ResourceNotFoundException("Ejercicio con ID "+attempt.getExercise_id()+" inexistente"));

        ExerciseProgress exercise_progress_obtained = exercise_progress_repo.findByUserIdAndExerciseExerciseId(attempt.getUser_id(), attempt.getExercise_id()).orElse(null);

        Boolean is_correct = exercise_validator.validate(exercise_attempt, attempt.getAnswer());

        if(exercise_progress_obtained != null ){
            if(!exercise_progress_obtained.getCompleted()) return existAttemptExerciseProgress(exercise_progress_obtained, is_correct);

            return Mapper.toDto(exercise_progress_obtained);
        } 
        

        ExerciseProgress new_exercise_progress = ExerciseProgress.builder()
                                                            .user(user_attempt)
                                                            .exercise(exercise_attempt)
                                                            .attempts(1)
                                                            .completed(is_correct)
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

    @Override
    public ExerciseProgressResponse findExerciseProgressByUserAndExercise(Long user_id, Long exercise_id) {
        if(user_id == null) throw new BadRequestException("Por favor, especifique el ID del Usuario");
        if(exercise_id == null) throw new BadRequestException("Por favor, especifique la ID del Ejercicio");

        if(!user_repo.existsById(user_id)) throw new ResourceNotFoundException("Usuario con ID "+user_id+" inexistente");
        if(!exercise_repo.existsById(exercise_id)) throw new ResourceNotFoundException("Ejercicio con ID "+exercise_id+" inexistente");

        ExerciseProgress exercise_progress_register = exercise_progress_repo.findByUserIdAndExerciseExerciseId(user_id, exercise_id).orElse(null);

        if(exercise_progress_register == null) throw new ResourceNotFoundException("Registro de Progreso Ejercicio del Usuario con ID '"+user_id+"' y Ejercicio con ID '"+exercise_id+"' inexistente");

        return Mapper.toDto(exercise_progress_register);
    }

    @Override
    public List<ExerciseProgressResponse> findAllExerciseProgressByUser(Long user_id) {
        if(user_id == null) throw new BadRequestException("Por favor, especifique el ID del Usuario");

        if(!user_repo.existsById(user_id)) throw new ResourceNotFoundException("Usuario con ID "+user_id+" inexistente");

        List<ExerciseProgress> list_exercise_progress = exercise_progress_repo.findByUserId(user_id);

        if(list_exercise_progress.isEmpty()) throw new ResourceNotFoundException("Registros de Progreso con Id Usuario '"+user_id+"' inexistentes");

        return list_exercise_progress.stream().map(Mapper::toDto).toList();
    }

}
