package com.joacocenteno.yoAprendo_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.CourseRequest;
import com.joacocenteno.yoAprendo_api.dto.CourseResponse;
import com.joacocenteno.yoAprendo_api.dto.LevelResponse;
import com.joacocenteno.yoAprendo_api.service.ICourseService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    ICourseService course_serv;


    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCoursesController() {
        return ResponseEntity.status(HttpStatus.OK).body(course_serv.getAllCourse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseByIdController(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(course_serv.getCourseById(id));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourseController(@Valid @RequestBody CourseRequest course_request) {
        
        CourseResponse course_created = course_serv.createCourse(course_request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(course_created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> editCourseController(@PathVariable Long id, @Valid @RequestBody CourseRequest course_request) {
        
        return ResponseEntity.status(HttpStatus.OK).body(course_serv.editCourse(id, course_request));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Void> toggleActiveController(@PathVariable Long id) {

        course_serv.toggleActiveCourse(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/levels")
    public ResponseEntity<List<LevelResponse>> getLevelsByCourseController(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(course_serv.getLevelsByCourse(id));
    }  

}
