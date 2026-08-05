package com.joacocenteno.yoAprendo_api.service;

import java.time.LocalDateTime;

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
    public ProgressResponse updateProgress(Long user_id, Long lessson_id) {
        if(user_id == null) throw new BadRequestException("Por favor, especifique la ID del usuario");
        if(lessson_id == null) throw new BadRequestException("Por favor, especifique la ID de la lección");

        if(!user_repo.existsById(user_id)) throw new ResourceNotFoundException("Usuario con ID "+ user_id + " inexistente");
        if(!lesson_repo.existsById(lessson_id)) throw new ResourceNotFoundException("Lección con ID "+ lessson_id + " inexistente");

        Long completed_count = exercise_progress_repo.countByUserIdAndExerciseLessonLessonIdAndCompletedTrue(user_id, lessson_id);
        Long total_exercises = exercise_repo.countByLessonLessonId(lessson_id);

        Double percent = total_exercises == 0
                ? 0.0
                : (completed_count.doubleValue() / total_exercises.doubleValue()) * 100.0;

        PorgressStatus status = resolveStatus(percent);

        Progress progress = progress_repo.findByUserIdAndLessonLessonId(user_id, lessson_id).orElse(null);

        if(progress == null){
            User user = user_repo.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "+ user_id + " inexistente"));
            Lesson lesson = lesson_repo.findById(lessson_id).orElseThrow(() -> new ResourceNotFoundException("Lección con ID "+ lessson_id + " inexistente"));

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
}
