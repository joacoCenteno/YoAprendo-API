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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.LevelRequest;
import com.joacocenteno.yoAprendo_api.dto.LevelResponse;
import com.joacocenteno.yoAprendo_api.dto.TopicResponse;
import com.joacocenteno.yoAprendo_api.service.ILevelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/levels")
public class LevelController {

    @Autowired
    ILevelService level_serv;


    @GetMapping
    public ResponseEntity<List<LevelResponse>> getAllLevelsController() {
        return ResponseEntity.status(HttpStatus.OK).body(level_serv.getAllLevel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LevelResponse> getLevelByIdController(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(level_serv.getLevelById(id));
    }

    @PostMapping
    public ResponseEntity<LevelResponse> createLevelController(@Valid @RequestBody LevelRequest level_request) {
        
        LevelResponse level_created = level_serv.createLevel(level_request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(level_created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LevelResponse> editLevelController(@PathVariable Long id, @Valid @RequestBody LevelRequest level_request) {
        
        return ResponseEntity.status(HttpStatus.OK).body(level_serv.editLevel(id, level_request));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Void> toggleActiveController(@PathVariable Long id) {

        level_serv.toggleActiveLevel(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/topics")
    public ResponseEntity<List<TopicResponse>> getTopicsByLevelController(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(level_serv.getTopicsByLevel(id));
    }  
}
