package com.joacocenteno.yoAprendo_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LevelRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String name;

    @Size(max = 500)
    String description;

    @NotNull
    @Positive
    Integer order;

    Boolean is_active;

    @NotNull
    Long curso_id;
}
