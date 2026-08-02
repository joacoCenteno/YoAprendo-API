package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.CourseRequest;
import com.joacocenteno.yoAprendo_api.dto.CourseResponse;
import com.joacocenteno.yoAprendo_api.dto.LevelResponse;

public interface ICourseService {
    public List<CourseResponse> getAllCourse();
    public CourseResponse getCourseById(Long course_id);
    public CourseResponse createCourse(CourseRequest course);
    public CourseResponse editCourse(Long course_id, CourseRequest course);
    public void toggleActiveCourse(Long course_id);
    public List<LevelResponse> getLevelsByCourse(Long course_id);
}
