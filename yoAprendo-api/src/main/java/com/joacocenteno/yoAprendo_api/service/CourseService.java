package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.joacocenteno.yoAprendo_api.dto.CourseRequest;
import com.joacocenteno.yoAprendo_api.dto.CourseResponse;
import com.joacocenteno.yoAprendo_api.dto.LevelResponse;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.DuplicateResourceException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Course;
import com.joacocenteno.yoAprendo_api.model.Level;
import com.joacocenteno.yoAprendo_api.repository.ICourseRepository;
import com.joacocenteno.yoAprendo_api.repository.ILevelRepository;

@Service
public class CourseService implements ICourseService{

    @Autowired
    ICourseRepository course_repo;

    @Autowired
    ILevelRepository level_repo;

    @Override
    public List<CourseResponse> getAllCourse() {
        return course_repo.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public CourseResponse getCourseById(Long course_id) {
        if(course_id == null) throw new BadRequestException("ID Curso no puede ser nulo");

        Course course_obtained = course_repo.findById(course_id).orElseThrow(() -> new ResourceNotFoundException("Curso con ID "+ course_id + " inexistente"));

        return Mapper.toDto(course_obtained);
    }

    @Override
    public CourseResponse createCourse(CourseRequest course) {
        if (course == null) throw new BadRequestException("Por favor, especifique los datos del curso");
        if(course_repo.existCourseByName(course.getName())) throw new DuplicateResourceException("Curso con nombre '"+ course.getName() + "' ya existente en la plataforma");

        Course course_created = Course.builder()
                                    .courseName(course.getName())
                                    .courseDescription(course.getDescription())
                                    .build();
        

        return Mapper.toDto(course_repo.save(course_created));
    }

    @Override
    public CourseResponse editCourse(Long course_id, CourseRequest course) {
        if(course_id == null) throw new BadRequestException("Por favor, especifique la ID");
        if (course == null) throw new BadRequestException("Por favor, especifique los datos del curso");

        Course course_modified = course_repo.findById(course_id).orElseThrow(() -> new ResourceNotFoundException("Curso con ID "+ course_id + " inexistente"));

        if(course.getName() != null && !course.getName().equals(course_modified.getCourseName())){

                if(course_repo.existCourseByName(course.getName())){
                    throw new DuplicateResourceException("Curso con nombre '"+ course.getName() + "' existente");
                }

                course_modified.setCourseName(course.getName());
        }
        if(course.getDescription() != null) course_modified.setCourseDescription(course.getDescription());
        if(course.getIs_active() != null) course_modified.setIsActive(course.getIs_active());

        return Mapper.toDto(course_repo.save(course_modified));
    }

    @Override
    public void toggleActiveCourse(Long course_id) {
        if(course_id == null) throw new BadRequestException("Por favor, especifique la ID");
        
        Course course_deactivated = course_repo.findById(course_id).orElseThrow(() -> new ResourceNotFoundException("Curso con ID "+ course_id + " inexistente"));

        course_deactivated.setIsActive(!course_deactivated.getIsActive());

        course_repo.save(course_deactivated);
    }

    @Override
    public List<LevelResponse> getLevelsByCourse(Long course_id) {
        if(course_id == null) throw new BadRequestException("Por favor, especifique la ID");

        if(!course_repo.existsById(course_id)) throw new ResourceNotFoundException("Curso con ID "+ course_id + " inexistente");

        List<Level> levels = level_repo.findByCourseCourseIdOrderByLevelOrder(course_id);

        return levels.stream().map(Mapper::toDto).toList();

        
    }

}
