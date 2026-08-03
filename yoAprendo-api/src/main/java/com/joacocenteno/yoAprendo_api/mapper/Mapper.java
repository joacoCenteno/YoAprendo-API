package com.joacocenteno.yoAprendo_api.mapper;

import com.joacocenteno.yoAprendo_api.dto.CecoeResponse;
import com.joacocenteno.yoAprendo_api.dto.CourseResponse;
import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressResponse;
import com.joacocenteno.yoAprendo_api.dto.ExerciseResponse;
import com.joacocenteno.yoAprendo_api.dto.LessonResponse;
import com.joacocenteno.yoAprendo_api.dto.LevelResponse;
import com.joacocenteno.yoAprendo_api.dto.ProgressResponse;
import com.joacocenteno.yoAprendo_api.dto.TopicResponse;
import com.joacocenteno.yoAprendo_api.dto.UserResponse;
import com.joacocenteno.yoAprendo_api.model.Cecoe;
import com.joacocenteno.yoAprendo_api.model.Course;
import com.joacocenteno.yoAprendo_api.model.Exercise;
import com.joacocenteno.yoAprendo_api.model.ExerciseProgress;
import com.joacocenteno.yoAprendo_api.model.Lesson;
import com.joacocenteno.yoAprendo_api.model.Level;
import com.joacocenteno.yoAprendo_api.model.Progress;
import com.joacocenteno.yoAprendo_api.model.Topic;
import com.joacocenteno.yoAprendo_api.model.User;

public class Mapper {

    public static CecoeResponse toDTO(Cecoe cecoe){
        if(cecoe == null) return null;

        return CecoeResponse.builder()
                        .id(cecoe.getCecoeId())
                        .name(cecoe.getCecoeName())
                        .is_active(cecoe.getIsActive())
                        .build();
    }

    public static CourseResponse toDto(Course course){
        if(course == null) return null;

        return CourseResponse.builder()
                        .id(course.getCourseId())
                        .name(course.getCourseName())
                        .description(course.getCourseDescription())
                        .build();
    }

    public static ExerciseProgressResponse toDto(ExerciseProgress exer_prog){
        if(exer_prog == null) return null;

        return ExerciseProgressResponse.builder()
                                    .id(exer_prog.getExerciseProgresId())
                                    .user_id(exer_prog.getUser().getId())
                                    .exercise_id(exer_prog.getExercise().getExerciseId())
                                    .attempts(exer_prog.getAttempts())
                                    .is_completed(exer_prog.getCompleted())
                                    .build();
    }

    public static ExerciseResponse toDto(Exercise exercise){
        if(exercise == null) return null;

        return ExerciseResponse.builder()
                            .id(exercise.getExerciseId())
                            .type(exercise.getExerciseType())
                            .json_content(exercise.getJsonContent())
                            .build();
    }

    public static LessonResponse toDto(Lesson lesson){
        if(lesson == null) return null;

        return LessonResponse.builder()
                            .id(lesson.getLessonId())
                            .title(lesson.getLessonTitle())
                            .description(lesson.getLessonDescription())
                            .order(lesson.getLessonOrder())
                            .topic_id(lesson.getTopic().getTopicId())
                            .build();
    }

    public static LevelResponse toDto(Level level){
        if(level == null) return null;

        return LevelResponse.builder()
                            .id(level.getLevelId())
                            .name(level.getLevelName())
                            .description(level.getLevelDescription())
                            .order(level.getLevelOrder())
                            .course_id(level.getCourse().getCourseId())
                            .build();
    }

    public static ProgressResponse toDto(Progress progress){
        if(progress == null) return null;

        return ProgressResponse.builder()
                            .id(progress.getProgressId())
                            .user_id(progress.getUser().getId())
                            .lesson_id(progress.getLesson().getLessonId())
                            .status(progress.getProgressStatus())
                            .percent(progress.getProgressPercent().intValue())
                            .build();
    }

    public static TopicResponse toDto(Topic topic){
        if(topic == null) return null;

        return TopicResponse.builder()
                            .id(topic.getTopicId())
                            .nombre(topic.getTopicName())
                            .description(topic.getTopicDescription())
                            .order(topic.getTopicOrder())
                            .level_id(topic.getLevel().getLevelId())
                            .build();
    }

    public static UserResponse toDto(User user){
        if(user == null) return null;

        return UserResponse.builder()
                            .id(user.getId())
                            .name(user.getUserName())
                            .surname(user.getUserSurname())
                            .rol(user.getUserRol())
                            .is_active(user.getUserActive())
                            .cecoe_id(user.getCecoe().getCecoeId())
                            .build();
    }
}
