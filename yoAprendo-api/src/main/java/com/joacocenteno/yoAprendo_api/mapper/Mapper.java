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
                        .id(cecoe.getCecoe_id())
                        .name(cecoe.getCecoe_name())
                        .is_active(cecoe.getIs_active())
                        .build();
    }

    public static CourseResponse toDto(Course course){
        if(course == null) return null;

        return CourseResponse.builder()
                        .id(course.getCourse_id())
                        .name(course.getCourse_name())
                        .description(course.getCourse_description())
                        .build();
    }

    public static ExerciseProgressResponse toDto(ExerciseProgress exer_prog){
        if(exer_prog == null) return null;

        return ExerciseProgressResponse.builder()
                                    .id(exer_prog.getExercise_progress_id())
                                    .user_id(exer_prog.getUser().getId())
                                    .exercise_id(exer_prog.getExercise().getExercise_id())
                                    .attempts(exer_prog.getAttempts())
                                    .is_completed(exer_prog.getCompleted())
                                    .build();
    }

    public static ExerciseResponse toDto(Exercise exercise){
        if(exercise == null) return null;

        return ExerciseResponse.builder()
                            .id(exercise.getExercise_id())
                            .type(exercise.getExercise_type())
                            .json_content(exercise.getJson_content())
                            .build();
    }

    public static LessonResponse toDto(Lesson lesson){
        if(lesson == null) return null;

        return LessonResponse.builder()
                            .id(lesson.getLesson_id())
                            .title(lesson.getLesson_title())
                            .description(lesson.getLesson_description())
                            .order(lesson.getLesson_order())
                            .topic_id(lesson.getTopic().getTopic_id())
                            .build();
    }

    public static LevelResponse toDto(Level level){
        if(level == null) return null;

        return LevelResponse.builder()
                            .id(level.getLevel_id())
                            .name(level.getLevel_name())
                            .description(level.getLevel_description())
                            .order(level.getLevel_order())
                            .course_id(level.getCourse().getCourse_id())
                            .build();
    }

    public static ProgressResponse toDto(Progress progress){
        if(progress == null) return null;

        return ProgressResponse.builder()
                            .id(progress.getProgress_id())
                            .user_id(progress.getUser().getId())
                            .lesson_id(progress.getLesson().getLesson_id())
                            .status(progress.getProgress_status())
                            .percent(progress.getProgress_percent().intValue())
                            .build();
    }

    public static TopicResponse toDto(Topic topic){
        if(topic == null) return null;

        return TopicResponse.builder()
                            .id(topic.getTopic_id())
                            .nombre(topic.getTopic_name())
                            .description(topic.getTopic_description())
                            .order(topic.getTopic_order())
                            .level_id(topic.getLevel().getLevel_id())
                            .build();
    }

    public static UserResponse toDto(User user){
        if(user == null) return null;

        return UserResponse.builder()
                            .id(user.getId())
                            .name(user.getUser_name())
                            .surname(user.getUser_surname())
                            .rol(user.getUser_rol())
                            .is_active(user.getUser_active())
                            .cecoe_id(user.getCecoe().getCecoe_id())
                            .build();
    }
}
