package com.joacocenteno.yoAprendo_api.dto;

import com.joacocenteno.yoAprendo_api.model.UserRol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    String apellido;

    @Size(min = 8)
    String password;

    @NotNull
    UserRol rol;

    Boolean is_active;

    Long cecoe_id;

}
