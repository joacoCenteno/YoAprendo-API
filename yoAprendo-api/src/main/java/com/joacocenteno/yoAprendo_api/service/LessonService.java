package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.ExerciseResponse;
import com.joacocenteno.yoAprendo_api.dto.LessonRequest;
import com.joacocenteno.yoAprendo_api.dto.LessonResponse;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.DuplicateResourceException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Exercise;
import com.joacocenteno.yoAprendo_api.model.ExerciseType;
import com.joacocenteno.yoAprendo_api.model.Lesson;
import com.joacocenteno.yoAprendo_api.model.Topic;
import com.joacocenteno.yoAprendo_api.repository.IExerciseRepository;
import com.joacocenteno.yoAprendo_api.repository.ILessonRepository;
import com.joacocenteno.yoAprendo_api.repository.ITopicRepository;

@Service
public class LessonService implements ILessonService{

    @Autowired
    ILessonRepository lesson_repo;

    @Autowired 
    ITopicRepository topic_repo;

    @Autowired
    IExerciseRepository exercise_repo;

    @Override
    public List<LessonResponse> getAllLesson() {
        return lesson_repo.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public LessonResponse getLessonById(Long lesson_id) {
        if(lesson_id == null) throw new BadRequestException("ID Lección no puede ser nulo");

        Lesson lesson_obtained = lesson_repo.findById(lesson_id)
                                            .orElseThrow(() -> new ResourceNotFoundException("Leccioón con ID "+ lesson_id + " inexistente"));
        
        return Mapper.toDto(lesson_obtained);
    }

    @Override
    public LessonResponse createLesson(LessonRequest lesson) {
        if(lesson == null) throw new BadRequestException("Por favor, especifique los datos de la lección");
        if(lesson.getTopic_id() == null) throw new BadRequestException("Por favor, especifique el ID del tema");

        Topic topic = topic_repo.findById(lesson.getTopic_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Tema con ID "+ lesson.getTopic_id() + " inexistente"));
        

        if(lesson.getTitle() != null && lesson_repo.existsByTopicTopicIdAndLessonTitle(lesson.getTopic_id(), lesson.getTitle())){
            throw new DuplicateResourceException("Lección '"+lesson.getTitle()+"' ya existente dentro del Tema '"+topic.getTopicName()+"'");
        }

        Lesson lesson_created = Lesson.builder()
                                    .lessonTitle(lesson.getTitle())
                                    .lessonDescription(lesson.getDescription())
                                    .lessonOrder(lesson.getOrder())
                                    .isactive(lesson.getIs_active() != null ? lesson.getIs_active() : true)
                                    .topic(topic)
                                    .build();

        lesson_created = lesson_repo.save(lesson_created);

        createDefaultExercises(lesson_created);

        return Mapper.toDto(lesson_created);

    }

    private void createDefaultExercises(Lesson lesson) {
        for (ExerciseType type : ExerciseType.values()) {
            Exercise exercise = Exercise.builder()
                    .exerciseType(type)
                    .lesson(lesson)
                    .build();

            exercise_repo.save(exercise);
        }
    }

    @Override
    public LessonResponse editLesson(Long lesson_id, LessonRequest lesson) {
        if(lesson_id == null) throw new BadRequestException("Por favor, especifique la ID");
        if(lesson == null) throw new BadRequestException("Por favor, especifique los datos de la lección");

        Lesson lesson_modified = lesson_repo.findById(lesson_id)
                                            .orElseThrow(() -> new ResourceNotFoundException("Lección con ID "+lesson_id+" inexistente"));

        Long topic_id_to_check = lesson.getTopic_id() != null ? lesson.getTopic_id() : lesson_modified.getTopic().getTopicId();

        if(lesson.getTitle() != null && !lesson.getTitle().equals(lesson_modified.getLessonTitle())){
            if(lesson_repo.existsByTopicTopicIdAndLessonTitle(topic_id_to_check, lesson.getTitle())){
                throw new DuplicateResourceException("Lección '"+lesson.getTitle()+"' ya existente dentro del Tema");
            }

            lesson_modified.setLessonTitle(lesson.getTitle());
        }

        if(lesson.getDescription() != null) lesson_modified.setLessonDescription(lesson.getDescription());
        if(lesson.getOrder() != null) lesson_modified.setLessonOrder(lesson.getOrder());
        if(lesson.getIs_active() != null) lesson_modified.setIsactive(lesson.getIs_active());

        if(lesson.getTopic_id() != null && !lesson.getTopic_id().equals(lesson_modified.getTopic().getTopicId())){
            Topic topic = topic_repo.findById(lesson.getTopic_id())
                                    .orElseThrow(() -> new ResourceNotFoundException("Tema con ID "+ lesson.getTopic_id() +" inexistente"));
            
            lesson_modified.setTopic(topic);
        }

        return Mapper.toDto(lesson_repo.save(lesson_modified));
    }

    @Override
    public void toggleActiveLesson(Long lesson_id) {
        if (lesson_id == null) throw new BadRequestException("Por favor, especifique la ID");

        Lesson lesson_deactivated = lesson_repo.findById(lesson_id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel con ID " + lesson_id + " inexistente"));

        lesson_deactivated.setIsactive(!lesson_deactivated.getIsactive());

        lesson_repo.save(lesson_deactivated);
    }

    @Override
    public List<ExerciseResponse> getExercisesByLesson(Long lesson_id) {
        if (lesson_id == null) throw new BadRequestException("Por favor, especifique la ID");

        if (!lesson_repo.existsById(lesson_id)) throw new ResourceNotFoundException("Lección con ID " + lesson_id + " inexistente");

        List<Exercise> exercises = exercise_repo.findByLessonLessonId(lesson_id);

        return exercises.stream().map(Mapper::toDto).toList();
    }
}
