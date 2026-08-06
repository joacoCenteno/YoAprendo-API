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

import com.joacocenteno.yoAprendo_api.dto.UserRequest;
import com.joacocenteno.yoAprendo_api.dto.UserResponse;
import com.joacocenteno.yoAprendo_api.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    IUserService user_serv;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsersController() {
        return ResponseEntity.status(HttpStatus.OK).body(user_serv.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserByIdController(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(user_serv.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUserController(@Valid @RequestBody UserRequest user_request) {
        
        UserResponse user_created = user_serv.createUser(user_request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(user_created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> editUserController(@PathVariable Long id, @Valid @RequestBody UserRequest user_request) {
        
        return ResponseEntity.status(HttpStatus.OK).body(user_serv.editUser(id, user_request));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Void> toggleActiveController(@PathVariable Long id) {

        user_serv.toggleActiveUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
