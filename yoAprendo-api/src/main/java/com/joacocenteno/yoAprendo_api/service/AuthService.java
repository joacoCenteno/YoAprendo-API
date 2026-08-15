package com.joacocenteno.yoAprendo_api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.LoginRequest;
import com.joacocenteno.yoAprendo_api.dto.TokenResponse;
import com.joacocenteno.yoAprendo_api.model.User;
import com.joacocenteno.yoAprendo_api.repository.IUserRepository;
import com.joacocenteno.yoAprendo_api.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public TokenResponse authenticate(final LoginRequest request){

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUserPlatformName(), request.getPassword())
        );

        final User user = userRepository.findByUserPlatformName(request.getUserPlatformName()).orElseThrow();

        final String accessToken = jwtService.generateToken(user);

        return TokenResponse.builder()
                            .access_token(accessToken)
                            .build();
    }


}
