package com.joacocenteno.yoAprendo_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.ProgressResponse;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Lesson;
import com.joacocenteno.yoAprendo_api.model.PorgressStatus;
import com.joacocenteno.yoAprendo_api.model.Progress;
import com.joacocenteno.yoAprendo_api.model.User;
import com.joacocenteno.yoAprendo_api.repository.IExerciseProgressRepository;
import com.joacocenteno.yoAprendo_api.repository.IExerciseRepository;
import com.joacocenteno.yoAprendo_api.repository.ILessonRepository;
import com.joacocenteno.yoAprendo_api.repository.IProgressRepository;
import com.joacocenteno.yoAprendo_api.repository.IUserRepository;

@Service
public class ProgressService implements IProgressService{

    @Autowired
    IProgressRepository progress_repo;

    @Autowired
    IUserRepository user_repo;

    @Autowired
    ILessonRepository lesson_repo;

    @Autowired
    IExerciseRepository exercise_repo;

    @Autowired
    IExerciseProgressRepository exercise_progress_repo;

    @Override
    public ProgressResponse updateProgress(Long user_id, Long lesson_id) {
        if(user_id == null) throw new BadRequestException("Por favor, especifique la ID del usuario");
        if(lesson_id == null) throw new BadRequestException("Por favor, especifique la ID de la lección");

        if(!user_repo.existsById(user_id)) throw new ResourceNotFoundException("Usuario con ID "+ user_id + " inexistente");
        if(!lesson_repo.existsById(lesson_id)) throw new ResourceNotFoundException("Lección con ID "+ lesson_id + " inexistente");

        Long completed_count = exercise_progress_repo.countByUserIdAndExerciseLessonLessonIdAndCompletedTrue(user_id, lesson_id);
        Long total_exercises = exercise_repo.countByLessonLessonId(lesson_id);

        Double percent = total_exercises == 0
                ? 0.0
                : (completed_count.doubleValue() / total_exercises.doubleValue()) * 100.0;

        PorgressStatus status = resolveStatus(percent);

        Progress progress = progress_repo.findByUserIdAndLessonLessonId(user_id, lesson_id).orElse(null);

        if(progress == null){
            User user = user_repo.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "+ user_id + " inexistente"));
            Lesson lesson = lesson_repo.findById(lesson_id).orElseThrow(() -> new ResourceNotFoundException("Lección con ID "+ lesson_id + " inexistente"));

            progress = Progress.builder()
                            .user(user)
                            .lesson(lesson)
                            .build();
        }

        progress.setProgressPercent(percent);
        progress.setProgressStatus(status);
        progress.setLastAccessDate(LocalDateTime.now());

        return Mapper.toDto(progress_repo.save(progress));
    }

    private PorgressStatus resolveStatus(Double percent) {
        if(percent == null || percent <= 0) return PorgressStatus.NOT_STARTED;
        if(percent >= 100) return PorgressStatus.COMPLETED;
        return PorgressStatus.IN_PROGRESS;
    }

    @Override
    public ProgressResponse findProgressByUserAndProgress(Long user_id, Long lesson_id) {
        if(user_id == null || lesson_id == null) throw new BadRequestException("Por favor, especifique la ID del Usuario o Lección");

        if(!user_repo.existsById(user_id)) throw new ResourceNotFoundException("Usuario con ID "+ user_id + " inexistente");
        if(!lesson_repo.existsById(lesson_id)) throw new ResourceNotFoundException("Lección con ID "+ lesson_id + " inexistente");


        Progress progress_obtained = progress_repo.findByUserIdAndLessonLessonId(user_id, lesson_id).orElse(null);

        if(progress_obtained == null) throw new ResourceNotFoundException("Progreso con Id Usuario '"+user_id+"' e Id Lección '"+lesson_id+" inexistente");

        return Mapper.toDto(progress_obtained);
    }

    @Override
    public List<ProgressResponse> findAllProgressByUser(Long user_id) {
        if(user_id == null) throw new BadRequestException("Por favor, especifique la ID del Usuario");

        if(!user_repo.existsById(user_id)) throw new ResourceNotFoundException("Usuario con ID "+user_id+" inexistente");

        List<Progress> progress = progress_repo.findByUserId(user_id);

        if(progress.isEmpty()) throw new ResourceNotFoundException("Registros de Progreso con Id Usuario '"+user_id+"' inexistentes");

        return progress.stream().map(Mapper::toDto).toList();
    }
}
