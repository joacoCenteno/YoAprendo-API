package com.joacocenteno.yoAprendo_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.ExerciseResponse;
import com.joacocenteno.yoAprendo_api.dto.UpdateExerciseRequest;
import com.joacocenteno.yoAprendo_api.service.IExerciseService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    IExerciseService exercise_serv;


    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(exercise_serv.getExerciseById(id));
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> editExerciseController(@PathVariable Long id, @Valid @RequestBody UpdateExerciseRequest exercise_request) {

        ExerciseResponse exercise_edited = exercise_serv.editExercise(id, exercise_request);

        return ResponseEntity.status(HttpStatus.OK).body(exercise_edited);
    }

}
