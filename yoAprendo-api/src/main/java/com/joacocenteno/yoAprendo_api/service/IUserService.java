package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.UserRequest;
import com.joacocenteno.yoAprendo_api.dto.UserResponse;

public interface IUserService {
    public List<UserResponse> getAllUsers();
    public UserResponse getUserById(Long userId);
    public UserResponse createUser(UserRequest user);
    public UserResponse editUser(Long userId, UserRequest user);
    public void toggleActiveUser(Long userId);
}
