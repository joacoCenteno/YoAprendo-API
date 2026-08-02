package com.joacocenteno.yoAprendo_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String name;

    @Size(max = 500)
    String description;

    Boolean is_active;
}
