package com.joacocenteno.yoAprendo_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressRequest;
import com.joacocenteno.yoAprendo_api.dto.ExerciseProgressResponse;
import com.joacocenteno.yoAprendo_api.service.IExerciseProgressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/exercise-progress")
public class ExerciseProgressController {

    @Autowired
    IExerciseProgressService exercise_progress_serv;

    @PostMapping("/attempt")
    public ResponseEntity<ExerciseProgressResponse> attemptExerciseProgressController(@RequestBody ExerciseProgressRequest attempt) {
        
        return ResponseEntity.status(HttpStatus.OK).body(exercise_progress_serv.attemptExerciseProgress(attempt));
    }

    @GetMapping("/user/{user_id}/exercise/{exercise_id}")
    public ResponseEntity<ExerciseProgressResponse> findExerciseProgressByUserAndExerciseController(
            @PathVariable Long user_id,
            @PathVariable Long exercise_id) {

        return ResponseEntity.status(HttpStatus.OK).body(exercise_progress_serv.findExerciseProgressByUserAndExercise(user_id, exercise_id));
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<ExerciseProgressResponse>> findAllExerciseProgressByUserController(@PathVariable Long user_id) {

        return ResponseEntity.status(HttpStatus.OK).body(exercise_progress_serv.findAllExerciseProgressByUser(user_id));
    }
    
}
