package com.joacocenteno.yoAprendo_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.ExerciseResponse;
import com.joacocenteno.yoAprendo_api.dto.LessonRequest;
import com.joacocenteno.yoAprendo_api.dto.LessonResponse;
import com.joacocenteno.yoAprendo_api.service.ILessonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    
    @Autowired
    ILessonService lesson_serv;

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getAllLessonsController() {
        return ResponseEntity.status(HttpStatus.OK).body(lesson_serv.getAllLesson());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getLessonByIdController(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(lesson_serv.getLessonById(id));
    }

    @PostMapping
    public ResponseEntity<LessonResponse> createLessonController(@Valid @RequestBody LessonRequest lesson_request) {
        
        LessonResponse lesson_created = lesson_serv.createLesson(lesson_request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(lesson_created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> editLessonController(@PathVariable Long id, @Valid @RequestBody LessonRequest lesson_request) {
        
        return ResponseEntity.status(HttpStatus.OK).body(lesson_serv.editLesson(id, lesson_request));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Void> toggleActiveController(@PathVariable Long id) {

        lesson_serv.toggleActiveLesson(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/exercises")
    public ResponseEntity<List<ExerciseResponse>> getExercisesByLessonController(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(lesson_serv.getExercisesByLesson(id));
    }  

}
