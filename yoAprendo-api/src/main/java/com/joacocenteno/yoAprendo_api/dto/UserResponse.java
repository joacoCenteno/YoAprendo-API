package com.joacocenteno.yoAprendo_api.dto;

import com.joacocenteno.yoAprendo_api.model.UserRol;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    Long id;
    String name;
    String surname;
    UserRol rol;
    Boolean is_active;
    Long cecoe_id;
}
