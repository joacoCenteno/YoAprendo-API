package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.ExerciseResponse;
import com.joacocenteno.yoAprendo_api.dto.LessonRequest;
import com.joacocenteno.yoAprendo_api.dto.LessonResponse;

public interface ILessonService {
    public List<LessonResponse> getAllLesson();
    public LessonResponse getLessonById(Long lesson_id);
    public LessonResponse createLesson(LessonRequest lesson);
    public LessonResponse editLesson(Long lesson_id, LessonRequest lesson);
    public void toggleActiveLesson(Long lesson_id);
    public List<ExerciseResponse> getExercisesByLesson(Long lesson_id);
}
