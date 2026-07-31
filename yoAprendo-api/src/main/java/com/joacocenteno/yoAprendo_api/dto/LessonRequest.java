package com.joacocenteno.yoAprendo_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LessonRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150)
    String title;

    @Size(max = 500)
    String description;

    @NotNull
    @Positive
    Integer order;

    @NotNull
    Long topic_id;
}

