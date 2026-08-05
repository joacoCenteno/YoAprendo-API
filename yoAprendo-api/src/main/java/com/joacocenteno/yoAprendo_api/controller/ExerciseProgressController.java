package com.joacocenteno.yoAprendo_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressRequest;
import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressResponse;
import com.joacocenteno.yoAprendo_api.service.IExerciseProgressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class ExerciseProgressController {

    @Autowired
    IExerciseProgressService exercise_progress_serv;

    @PostMapping("/attempt")
    public ResponseEntity<ExerciseProgressResponse> postMethodName(@RequestBody ExerciseProgressRequest attempt) {
        
        return ResponseEntity.status(HttpStatus.OK).body(exercise_progress_serv.attemptExerciseProgress(attempt));
    }
    
}
