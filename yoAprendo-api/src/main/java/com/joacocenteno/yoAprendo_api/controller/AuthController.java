package com.joacocenteno.yoAprendo_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.LoginRequest;
import com.joacocenteno.yoAprendo_api.dto.TokenResponse;
import com.joacocenteno.yoAprendo_api.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest request) {
        
        final TokenResponse response = authService.authenticate(request);
    
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
