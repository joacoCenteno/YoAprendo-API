package com.joacocenteno.yoAprendo_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    @NotBlank(message = "El usuario es oblitagorio")
    private String userPlatformName;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
