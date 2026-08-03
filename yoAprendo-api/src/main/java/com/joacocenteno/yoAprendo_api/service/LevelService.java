package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.LevelRequest;
import com.joacocenteno.yoAprendo_api.dto.LevelResponse;
import com.joacocenteno.yoAprendo_api.dto.TopicResponse;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.DuplicateResourceException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Course;
import com.joacocenteno.yoAprendo_api.model.Level;
import com.joacocenteno.yoAprendo_api.model.Topic;
import com.joacocenteno.yoAprendo_api.repository.ICourseRepository;
import com.joacocenteno.yoAprendo_api.repository.ILevelRepository;
import com.joacocenteno.yoAprendo_api.repository.ITopicRepository;

@Service
public class LevelService implements ILevelService {

    @Autowired
    ILevelRepository level_repo;

    @Autowired
    ICourseRepository course_repo;

    @Autowired
    ITopicRepository topic_repo;

    @Override
    public List<LevelResponse> getAllLevel() {
        return level_repo.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public LevelResponse getLevelById(Long level_id) {
        if (level_id == null) throw new BadRequestException("ID Nivel no puede ser nulo");

        Level level_obtained = level_repo.findById(level_id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel con ID " + level_id + " inexistente"));

        return Mapper.toDto(level_obtained);
    }

    @Override
    public LevelResponse createLevel(LevelRequest level) {
        if (level == null) throw new BadRequestException("Por favor, especifique los datos del nivel");
        if (level.getCurso_id() == null) throw new BadRequestException("Por favor, especifique el ID del curso");

        Course course = course_repo.findById(level.getCurso_id())
                .orElseThrow(() -> new ResourceNotFoundException("Curso con ID " + level.getCurso_id() + " inexistente"));

        if (level.getName() != null && level_repo.existsByCourseCourseIdAndLevelName(level.getCurso_id(), level.getName())) {
            throw new DuplicateResourceException("Nivel con nombre '" + level.getName() + "' ya existente en el curso");
        }

        Level level_created = Level.builder()
                .levelName(level.getName())
                .levelDescription(level.getDescription())
                .levelOrder(level.getOrder())
                .isActive(level.getIs_active() != null ? level.getIs_active() : true)
                .course(course)
                .build();

        return Mapper.toDto(level_repo.save(level_created));
    }

    @Override
    public LevelResponse editLevel(Long id, LevelRequest level) {
        if (id == null) throw new BadRequestException("Por favor, especifique la ID");
        if (level == null) throw new BadRequestException("Por favor, especifique los datos del nivel");

        Level level_modified = level_repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel con ID " + id + " inexistente"));

        Long course_id_to_check = level.getCurso_id() != null ? level.getCurso_id() : level_modified.getCourse().getCourseId();

        if (level.getName() != null && !level.getName().equals(level_modified.getLevelName())) {
            if (level_repo.existsByCourseCourseIdAndLevelName(course_id_to_check, level.getName())) {
                throw new DuplicateResourceException("Nivel con nombre '" + level.getName() + "' ya existente en el curso");
            }
            level_modified.setLevelName(level.getName());
        }

        if (level.getDescription() != null) level_modified.setLevelDescription(level.getDescription());
        if (level.getOrder() != null) level_modified.setLevelOrder(level.getOrder());
        if (level.getIs_active() != null) level_modified.setIsActive(level.getIs_active());

        if (level.getCurso_id() != null && !level.getCurso_id().equals(level_modified.getCourse().getCourseId())) {
            Course course = course_repo.findById(level.getCurso_id())
                    .orElseThrow(() -> new DuplicateResourceException("Curso con ID " + level.getCurso_id() + " inexistente"));
            level_modified.setCourse(course);
        }

        return Mapper.toDto(level_repo.save(level_modified));
    }

    @Override
    public void toggleActiveLevel(Long id) {
        if (id == null) throw new BadRequestException("Por favor, especifique la ID");

        Level level_deactivated = level_repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel con ID " + id + " inexistente"));

        level_deactivated.setIsActive(!level_deactivated.getIsActive());

        level_repo.save(level_deactivated);
    }

    @Override
    public List<TopicResponse> getTopicsByLevel(Long level_id) {
        if (level_id == null) throw new BadRequestException("Por favor, especifique la ID");

        if (!level_repo.existsById(level_id)) throw new ResourceNotFoundException("Nivel con ID " + level_id + " inexistente");

        List<Topic> topics = topic_repo.findByLevelLevelIdOrderByTopicOrder(level_id);

        return topics.stream().map(Mapper::toDto).toList();
    }
}
