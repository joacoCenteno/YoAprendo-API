package com.joacocenteno.yoAprendo_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.ProgressResponse;
import com.joacocenteno.yoAprendo_api.service.IProgressService;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    IProgressService progress_serv;

    @GetMapping("/user/{user_id}/lesson/{lesson_id}")
    public ResponseEntity<ProgressResponse> findProgressByUserAndLessonController(
            @PathVariable Long user_id,
            @PathVariable Long lesson_id) {

        return ResponseEntity.status(HttpStatus.OK).body(progress_serv.findProgressByUserAndProgress(user_id, lesson_id));
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<ProgressResponse>> findAllProgressByUserController(@PathVariable Long user_id) {

        return ResponseEntity.status(HttpStatus.OK).body(progress_serv.findAllProgressByUser(user_id));
    }

}
