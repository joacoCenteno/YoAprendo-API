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

import com.joacocenteno.yoAprendo_api.dto.LessonResponse;
import com.joacocenteno.yoAprendo_api.dto.TopicRequest;
import com.joacocenteno.yoAprendo_api.dto.TopicResponse;
import com.joacocenteno.yoAprendo_api.service.ITopicService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    ITopicService topic_serv;

    @GetMapping
    public ResponseEntity<List<TopicResponse>> getAllTopicsController() {
        return ResponseEntity.status(HttpStatus.OK).body(topic_serv.getAllTopic());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> getTopicByIdController(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(topic_serv.getTopicById(id));
    }

    @PostMapping
    public ResponseEntity<TopicResponse> createTopicController(@Valid @RequestBody TopicRequest topic_request) {
        
        TopicResponse topic_created = topic_serv.createTopic(topic_request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(topic_created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicResponse> editTopicController(@PathVariable Long id, @Valid @RequestBody TopicRequest topic_request) {
        
        return ResponseEntity.status(HttpStatus.OK).body(topic_serv.editTopic(id, topic_request));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Void> toggleActiveController(@PathVariable Long id) {

        topic_serv.toggleActiveTopic(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<LessonResponse>> getLessonsByTopicController(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(topic_serv.getLessonsByTopic(id));
    }  
}
